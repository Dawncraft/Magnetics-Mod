package io.github.dawncraft.magnetics.block;

import io.github.dawncraft.magnetics.MagneticsMod;
import io.github.dawncraft.magnetics.container.ModGuiHandler;
import io.github.dawncraft.magnetics.stats.ModStats;
import io.github.dawncraft.magnetics.tileentity.TileEntityLightningArrester;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLightningArrester extends BlockContainer
{
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 1.0D, 0.875D);

    public BlockLightningArrester(Material material)
    {
        super(material);
        this.setHardness(1.2f);
        this.setSoundType(SoundType.METAL);
        this.setHarvestLevel("pickaxe", 0);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return AABB;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityLightningArrester();
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World world, BlockPos pos)
    {
        return Container.calcRedstone(world.getTileEntity(pos));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
            TileEntity tileentity = world.getTileEntity(pos);
            if (tileentity instanceof TileEntityLightningArrester)
            {
                player.openGui(MagneticsMod.instance, ModGuiHandler.GUI_LIGHTNING_ARRESTER, world, pos.getX(), pos.getY(), pos.getZ());
                player.addStat(ModStats.LIGHTNING_ARRESTER_INTERACTION);
            }
        }
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {

    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
}
