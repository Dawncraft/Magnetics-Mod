package io.github.dawncraft.magnetics.client.renderer.block;

import io.github.dawncraft.magnetics.MagneticsMod;
import io.github.dawncraft.magnetics.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Register custom blocks' model.
 *
 * @author QingChenW
 */
@Mod.EventBusSubscriber(modid = MagneticsMod.MODID, value = Side.CLIENT)
public class ModBlockModels
{
    @SubscribeEvent
    public static void registerBlockModels(ModelRegistryEvent event)
    {
        registerStateMapper(ModBlocks.MAGNET_DOOR, new StateMap.Builder().ignore(BlockDoor.POWERED).build());

        registerBuiltIn(ModBlocks.MAGNET_CHEST, "minecraft:blocks/planks_oak");
    }

    @SubscribeEvent
    public static void blockColors(ColorHandlerEvent.Block event)
    {
    	BlockColors blockColorHandler = event.getBlockColors();
    }

    /**
     * Register a block state mapper.
     *
     * @param block block to register
     * @param mapper block's state mapper
     */
    private static void registerStateMapper(Block block, IStateMapper mapper)
    {
        ModelLoader.setCustomStateMapper(block, mapper);
    }

    /**
     * Register a builtin block.
     *
     * @param block builtin block to register
     * @param iconName the name of texture
     */
    private static void registerBuiltIn(Block block, String iconName)
    {
        //DawnClientHooks.registerBuiltInBlocks(block);
        //DawnClientHooks.registerBlockParticle(block, iconName);
    }
}
