package io.github.dawncraft.magnetics.recipe;

import io.github.dawncraft.magnetics.CommonProxy;
import io.github.dawncraft.magnetics.MagneticsMod;
import io.github.dawncraft.magnetics.api.recipe.LightningStrikeRecipeManager;
import io.github.dawncraft.magnetics.block.ModBlocks;
import io.github.dawncraft.magnetics.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Register mod recipes.
 *
 * @author QingChenW
 */
@Mod.EventBusSubscriber(modid = MagneticsMod.MODID)
public class ModRecipes
{
    public static void init()
    {
        registerSmelting(ModBlocks.MAGNET_ORE, new ItemStack(ModItems.MAGNET_INGOT), 0.7F);

        registerLightningStrikeRecipe(Items.IRON_INGOT, new ItemStack(ModItems.MAGNET_INGOT));
        registerLightningStrikeRecipe(ModItems.MAGNET_SWORD, new MagnetWeaponRecipe());
        registerLightningStrikeRecipe(ModItems.MAGNET_WAND, new MagnetWeaponRecipe());

        if (CommonProxy.isIC2Loaded)
        {
            registerLightningStrikeRecipe(ModItems.RE_BATTERY, new LightningStrikeRecipeManager.IRecipe()
            {
                @Override
                public boolean matches(ItemStack input)
                {
                    return true;
                }

                @Override
                public ItemStack getResult(ItemStack input)
                {
                    ItemStack output = input.copy();
                    if (!output.hasTagCompound()) output.setTagCompound(new NBTTagCompound());
                    output.getTagCompound().setDouble("charge", 10000.0D);
                    return output;
                }
            });
            registerLightningStrikeRecipe(ModItems.ADVANCED_RE_BATTERY, new LightningStrikeRecipeManager.IRecipe()
            {
                @Override
                public boolean matches(ItemStack input)
                {
                    return true;
                }

                @Override
                public ItemStack getResult(ItemStack input)
                {
                    ItemStack output = input.copy();
                    if (!output.hasTagCompound()) output.setTagCompound(new NBTTagCompound());
                    output.getTagCompound().setDouble("charge", 100000.0D);
                    return input;
                }
            });
        }
    }

    @SubscribeEvent
    public static void getFurnaceFuelValue(FurnaceFuelBurnTimeEvent event)
    {
        if (event.getItemStack().getItem() == ModItems.MAGNET_DOOR)
        {
            event.setBurnTime(0);
        }
    }

    private static void registerSmelting(Item input, ItemStack output, float xp)
    {
        GameRegistry.addSmelting(input, output, xp);
    }

    private static void registerSmelting(Block input, ItemStack output, float xp)
    {
        GameRegistry.addSmelting(input, output, xp);
    }

    private static void registerLightningStrikeRecipe(Item input, ItemStack output)
    {
        registerLightningStrikeRecipe(input, new LightningStrikeRecipeManager.ItemRecipe(input, output));
    }

    private static void registerLightningStrikeRecipe(Block input, ItemStack output)
    {
        registerLightningStrikeRecipe(Item.getItemFromBlock(input), output);
    }

    private static void registerLightningStrikeRecipe(Item input, LightningStrikeRecipeManager.IRecipe recipe)
    {
        LightningStrikeRecipeManager.getInstance().addRecipe(input, recipe);
    }

    private static void registerLightningStrikeRecipe(Block input, LightningStrikeRecipeManager.IRecipe recipe)
    {
        registerLightningStrikeRecipe(Item.getItemFromBlock(input), recipe);
    }

    private static class MagnetWeaponRecipe implements LightningStrikeRecipeManager.IRecipe
    {
        @Override
        public boolean matches(ItemStack input)
        {
            if (input.hasTagCompound() && input.getTagCompound().getBoolean("isPowered")) return false;
            return true;
        }

        @Override
        public ItemStack getResult(ItemStack input)
        {
            ItemStack output = input.copy();
            if (!output.hasTagCompound()) output.setTagCompound(new NBTTagCompound());
            NBTTagCompound nbt = output.getTagCompound();
            nbt.setBoolean("isPowered", true);
            return output;
        }
    }
}
