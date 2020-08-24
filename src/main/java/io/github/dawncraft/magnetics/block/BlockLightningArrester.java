package io.github.dawncraft.magnetics.block;

import io.github.dawncraft.magnetics.MagneticsMod;
import io.github.dawncraft.magnetics.container.ModGuiHandler;
import io.github.dawncraft.magnetics.stats.ModStats;
import io.github.dawncraft.magnetics.tileentity.TileEntityLightningArrester;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

/**
 * Lightning arrester
 *
 * @author QingChenW
 */
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
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return AABB;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.CENTER_BIG;
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
        TileEntity tileEntity = world.getTileEntity(pos);
        IItemHandler itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
        return ItemHandlerHelper.calcRedstoneFromInventory(itemHandler);
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
        TileEntity tileEntity = world.getTileEntity(pos);
        IItemHandler itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
        if (itemHandler != null)
        {
            for (int i = 0; i < itemHandler.getSlots(); ++i)
            {
                ItemStack itemStack = itemHandler.getStackInSlot(i);
                if (!itemStack.isEmpty())
                {
                    InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemStack);
                }
            }
            world.updateComparatorOutputLevel(pos, this);
        }
    }
}
