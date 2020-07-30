package io.github.dawncraft.magnetics.client.renderer.item;

import io.github.dawncraft.magnetics.MagneticsMod;
import io.github.dawncraft.magnetics.block.ModBlocks;
import io.github.dawncraft.magnetics.client.renderer.tileentity.ModTileEntityItemStackRenderer;
import io.github.dawncraft.magnetics.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Register items' inventory model.(Include ItemBlock)
 *
 * @author QingChenW
 */
@Mod.EventBusSubscriber(modid = MagneticsMod.MODID, value = Side.CLIENT)
public class ModItemModels
{
    @SubscribeEvent
    public static void registerItemModels(ModelRegistryEvent event)
    {
        registerBlock(ModBlocks.MAGNET_ORE);
        registerBlock(ModBlocks.MAGNET_BLOCK);

        registerBlock(ModBlocks.MAGNET_CHEST);

        registerItem(ModItems.MAGNET_DOOR);
        registerBlock(ModBlocks.MAGNET_RAIL);
        registerItem(ModItems.MAGNET_CARD);

        registerItem(ModItems.MAGNET);
        registerItem(ModItems.MAGNET_INGOT);
        registerItem(ModItems.MAGNET_STICK);
        registerItem(ModItems.MAGNET_BALL);

        registerItem(ModItems.MAGNET_AXE);
        registerItem(ModItems.MAGNET_PICKAXE);
        registerItem(ModItems.MAGNET_SHOVEL);
        registerItem(ModItems.MAGNET_HOE);
        // registerItem(ModItems.MAGNET_HAMMER);

        registerItem(ModItems.MAGNET_SWORD);
        registerItem(ModItems.MAGNET_WAND);
        registerItem(ModItems.MAGNET_HELMET);
        registerItem(ModItems.MAGNET_CHESTPLATE);
        registerItem(ModItems.MAGNET_LEGGINGS);
        registerItem(ModItems.MAGNET_BOOTS);

        // TEISR
        registerTEISR(ModBlocks.MAGNET_CHEST, ModTileEntityItemStackRenderer.instance);
    }

    @SubscribeEvent
    public static void itemColors(ColorHandlerEvent.Item event)
    {
        ItemColors itemColorHandler = event.getItemColors();
    }

    /**
     * Register a item's inventory model and its variants.
     *
     * @param item item's string id
     */
    private static void registerItem(Item item)
    {
        registerItem(item, 0, item.getRegistryName().toString());
    }

    /**
     * Register a block's inventory model and its variants.
     *
     * @param block block's string id
     */
    private static void registerBlock(Block block)
    {
        registerBlock(block, 0, block.getRegistryName().toString());
    }

    /**
     * Register a item's inventory model with meta and name and its variants.
     *
     * @param item item to register
     * @param meta item's meta
     * @param name item's model name
     */
    private static void registerItem(Item item, int meta, String name)
    {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(name, "inventory"));
    }

    /**
     * Register a block's inventory model with meta and name and its variants.
     *
     * @param block block to register
     * @param meta  item's meta
     * @param name  item's model name
     */
    private static void registerBlock(Block block, int meta, String name)
    {
        registerItem(Item.getItemFromBlock(block), meta, name);
    }

    /**
     * Register a tileentity itemstack renderer for an item.
     *
     * @param item  the item to register
     * @param teisr the teisr of item
     */
    private static void registerTEISR(Item item, TileEntityItemStackRenderer teisr)
    {
        item.setTileEntityItemStackRenderer(teisr);
    }

    /**
     * Register a tileentity itemstack renderer for a block's item.
     *
     * @param block the block to register
     * @param teisr the teisr of block
     */
    private static void registerTEISR(Block block, TileEntityItemStackRenderer teisr)
    {
        registerTEISR(Item.getItemFromBlock(block), teisr);
    }
}
