package io.github.dawncraft.magnetics.block;

import io.github.dawncraft.magnetics.tileentity.TileEntityMagnetChest;
import net.minecraft.block.BlockChest;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * A magnet chest which can attract items around it.
 *
 * @author QingChenW
 **/
public class BlockMagnetChest extends BlockChest
{
    public BlockMagnetChest()
    {
        super(Type.BASIC);
        this.setHardness(2.5F);
        this.setSoundType(SoundType.WOOD);
        this.setHarvestLevel("axe", 0);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityMagnetChest();
    }

    @Override
    public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity)
    {
        if (!world.isRemote && entity.isEntityAlive())
        {
            if (entity instanceof EntityItem)
            {
                TileEntityMagnetChest tileEntityMagnetChest = (TileEntityMagnetChest) world.getTileEntity(pos);
                tileEntityMagnetChest.insertStackFromEntity((EntityItem) entity);
            }
        }
    }
}
