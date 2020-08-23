package io.github.dawncraft.magnetics.item;

import io.github.dawncraft.magnetics.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Register ore dictionary.
 *
 * @author QingChenW
 */
public class ModOreDictionary
{
    public static void init()
    {
        registerOre("magnet", ModItems.MAGNET);
        registerOre("ingotMagnet", ModItems.MAGNET_INGOT);
        registerOre("blockMagnet", ModBlocks.MAGNET_BLOCK);
    }

    public static void registerOre(String name, Item ore)
    {
        OreDictionary.registerOre(name, ore);
    }

    public static void registerOre(String name, Block ore)
    {
        OreDictionary.registerOre(name, ore);
    }

    public static void registerOre(String name, ItemStack ore)
    {
        OreDictionary.registerOre(name, ore);
    }
}
