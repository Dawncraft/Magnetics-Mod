package io.github.dawncraft.magnetics.block;

import io.github.dawncraft.magnetics.ConfigLoader;
import io.github.dawncraft.magnetics.MagneticsMod;
import io.github.dawncraft.magnetics.creativetab.ModCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Register mod blocks.
 *
 * @author QingChenW
 */
@Mod.EventBusSubscriber(modid = MagneticsMod.MODID)
public class ModBlocks
{
    public static Block MAGNET_ORE;
    public static Block MAGNET_BLOCK;
    
    public static Block MAGNET_CHEST;
    
    public static Block MAGNET_DOOR;
    public static Block MAGNET_RAIL;
	
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        boolean flag = ConfigLoader.useSeparateCreativeTab;
        
    	event.getRegistry().registerAll(
    			MAGNET_ORE = new BlockOre().setCreativeTab(flag ? ModCreativeTabs.MAGNETICS : CreativeTabs.BUILDING_BLOCKS).setTranslationKey("magnetOre").setRegistryName("magnet_ore"),
    			MAGNET_BLOCK = new Block(Material.IRON).setHardness(5.0f).setResistance(10.0f).setSoundType(SoundType.METAL).setCreativeTab(flag ? ModCreativeTabs.MAGNETICS : CreativeTabs.BUILDING_BLOCKS).setTranslationKey("magnetBlock").setRegistryName("magnet_block"),
    			MAGNET_CHEST = new BlockMagnetChest().setCreativeTab(flag ? ModCreativeTabs.MAGNETICS : CreativeTabs.DECORATIONS).setTranslationKey("magnetChest").setRegistryName("magnet_chest"),
    	        MAGNET_DOOR = new BlockMagnetDoor().setCreativeTab(flag ? ModCreativeTabs.MAGNETICS : CreativeTabs.REDSTONE).setTranslationKey("magnetDoor").setRegistryName("magnet_door"),
    	        MAGNET_RAIL = new BlockMagnetRail().setCreativeTab(flag ? ModCreativeTabs.MAGNETICS : CreativeTabs.TRANSPORTATION).setTranslationKey("magnetRail").setRegistryName("magnet_rail")
    			);
    			
        MAGNET_ORE.setHarvestLevel("pickaxe", 1);
    }
}
