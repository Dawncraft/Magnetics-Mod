package io.github.dawncraft.magnetics.client.renderer.tileentity;

import io.github.dawncraft.magnetics.block.ModBlocks;
import io.github.dawncraft.magnetics.tileentity.TileEntityMagnetChest;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * Register tileentity renderer.
 *
 * @author QingChenW
 */
public class ModTileEntityRenderers
{
    public static void init()
    {
        registerTileEntityRenderer(TileEntityMagnetChest.class, new TileEntityRendererChest());
    }
    
    /**
     * Register a tileentity's renderer.
     *
     * @param tileEntityClass the class of tileentity
     * @param renderer the renderer of tileentity
     */
    private static <T extends TileEntity> void registerTileEntityRenderer(Class<T> tileEntityClass, TileEntitySpecialRenderer<? super T> renderer)
    {
        ClientRegistry.bindTileEntitySpecialRenderer(tileEntityClass, renderer);
    }


}
