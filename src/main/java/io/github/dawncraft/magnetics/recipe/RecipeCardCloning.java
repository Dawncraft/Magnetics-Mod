package io.github.dawncraft.magnetics.recipe;

import java.util.UUID;

import io.github.dawncraft.magnetics.item.ItemMagnetCard;
import io.github.dawncraft.magnetics.item.ModItems;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeCardCloning extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{
    @Override
    public boolean isDynamic()
    {
        return true;
    }

    @Override
    public boolean canFit(int width, int height)
    {
        return width >= 3 && height >= 3;
    }

    @Override
    public ItemStack getRecipeOutput()
    {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World world)
    {
        ItemStack source = ItemStack.EMPTY;
        int targetCount = 0;

        for (int j = 0; j < inv.getSizeInventory(); ++j)
        {
            ItemStack stack = inv.getStackInSlot(j);
            if (!stack.isEmpty())
            {
                if (stack.getItem() == ModItems.MAGNET_CARD)
                {
                    if (stack.hasTagCompound())
                    {
                        if (!source.isEmpty())
                        {
                            return false;
                        }
                        source = stack;
                    }
                    else
                    {
                        ++targetCount;
                    }
                }
                else
                {
                    return false;
                }
            }
        }

        return !source.isEmpty() && targetCount > 0;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        ItemStack source = ItemStack.EMPTY;
        int targetCount = 0;

        for (int j = 0; j < inv.getSizeInventory(); ++j)
        {
            ItemStack stack = inv.getStackInSlot(j);
            if (!stack.isEmpty())
            {
                if (stack.getItem() == ModItems.MAGNET_CARD)
                {
                    if (stack.hasTagCompound())
                    {
                        if (!source.isEmpty())
                        {
                            return ItemStack.EMPTY;
                        }
                        source = stack;
                    }
                    else
                    {
                        ++targetCount;
                    }
                }
                else
                {
                    return ItemStack.EMPTY;
                }
            }
        }

        if (!source.isEmpty() && targetCount >= 1)
        {
            ItemStack target = new ItemStack(ModItems.MAGNET_CARD, targetCount);
            NBTTagCompound nbt = source.getTagCompound().copy();
            nbt.setString("UUID", UUID.randomUUID().toString());
            target.setTagCompound(nbt);
            return target;
        }
        else
        {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
    {
        NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(inv.getSizeInventory(), ItemStack.EMPTY);
        for (int i = 0; i < nonnulllist.size(); ++i)
        {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.getItem() instanceof ItemMagnetCard && stack.hasTagCompound())
            {
                ItemStack oldstack = stack.copy();
                oldstack.setCount(1);
                nonnulllist.set(i, oldstack);
                break;
            }
        }
        return nonnulllist;
    }
}
