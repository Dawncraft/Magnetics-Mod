package io.github.dawncraft.magnetics.client.renderer.tileentity;

import io.github.dawncraft.magnetics.block.ModBlocks;
import io.github.dawncraft.magnetics.tileentity.TileEntityMagnetChest;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * The mod's TileEntityItemStackRenderer
 *
 * @author QingChenW
 */
public class ModTileEntityItemStackRenderer extends TileEntityItemStackRenderer
{
    public static final ModTileEntityItemStackRenderer instance;

    private final TileEntityMagnetChest chestMagnet = new TileEntityMagnetChest();

    @Override
    public void renderByItem(ItemStack itemStack, float partialTicks)
    {
        Item item = itemStack.getItem();
        if (item == Item.getItemFromBlock(ModBlocks.MAGNET_CHEST))
        {
            TileEntityRendererDispatcher.instance.render(this.chestMagnet, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks);
        }
    }

    static
    {
        instance = new ModTileEntityItemStackRenderer();
    }
}
