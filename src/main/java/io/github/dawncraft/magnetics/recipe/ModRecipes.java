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
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.RegistryEvent;
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
        registerLightningStrikeRecipe(ModItems.MAGNET_SWORD, new MagnetWeaponRecipe()
        {
            @Override
            public ItemStack getInputDelegate()
            {
                return new ItemStack(ModItems.MAGNET_SWORD);
            }
        });
        registerLightningStrikeRecipe(ModItems.MAGNET_WAND, new MagnetWeaponRecipe()
        {
            @Override
            public ItemStack getInputDelegate()
            {
                return new ItemStack(ModItems.MAGNET_WAND);
            }
        });
        if (CommonProxy.isIC2Loaded)
        {
            if (ModItems.RE_BATTERY != null)
                registerLightningStrikeRecipe(ModItems.RE_BATTERY, new BatteryChargeRecipe(ModItems.RE_BATTERY));
            if (ModItems.ADVANCED_RE_BATTERY != null)
                registerLightningStrikeRecipe(ModItems.ADVANCED_RE_BATTERY, new BatteryChargeRecipe(ModItems.ADVANCED_RE_BATTERY));
            if (ModItems.ITEMBATRE != null)
                registerLightningStrikeRecipe(ModItems.ITEMBATRE, new BatteryChargeRecipe(ModItems.ITEMBATRE));
        }
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event)
    {
        event.getRegistry().registerAll(new RecipeCardCloning().setRegistryName("cardcloning"));
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

    private static abstract class MagnetWeaponRecipe implements LightningStrikeRecipeManager.IRecipe
    {
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

    private static class BatteryChargeRecipe implements LightningStrikeRecipeManager.IRecipe
    {
        private final Item input;

        public BatteryChargeRecipe(Item input)
        {
            this.input = input;
        }

        @Override
        public ItemStack getInputDelegate()
        {
            ItemStack stack = new ItemStack(this.input);
            stack.setTagCompound(new NBTTagCompound());
            if (CommonProxy.isIC2Loaded)
            {
                if (this.input == ModItems.RE_BATTERY || this.input == ModItems.ADVANCED_RE_BATTERY ||
                    this.input == ModItems.ITEMBATRE)
                {
                    stack.getTagCompound().setDouble("charge", 0.0D);
                }
            }
            return stack;
        }

        @Override
        public ItemStack getResult(ItemStack input)
        {
            ItemStack output = input.copy();
            if (!output.hasTagCompound()) output.setTagCompound(new NBTTagCompound());
            if (CommonProxy.isIC2Loaded)
            {
                if (this.input == ModItems.RE_BATTERY || this.input == ModItems.ITEMBATRE)
                {
                    output.getTagCompound().setDouble("charge", 10000.0D);
                }
                else if (this.input == ModItems.ADVANCED_RE_BATTERY)
                {
                    output.getTagCompound().setDouble("charge", 100000.0D);
                }
            }
            return output;
        }
    }
}
