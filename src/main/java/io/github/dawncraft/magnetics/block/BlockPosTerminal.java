package io.github.dawncraft.magnetics.block;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import io.github.dawncraft.magnetics.CommonProxy;
import io.github.dawncraft.magnetics.MagneticsMod;
import io.github.dawncraft.magnetics.api.item.IItemCard;
import io.github.dawncraft.magnetics.container.ModGuiHandler;
import io.github.dawncraft.magnetics.stats.ModStats;
import io.github.dawncraft.magnetics.tileentity.TileEntityPosTerminal;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

/**
 * POS Terminal
 *
 * @author QingChenW
 */
@Optional.Interface(iface = "dan200.computercraft.api.peripheral.IPeripheralProvider", modid = CommonProxy.CC_MODID)
public class BlockPosTerminal extends BlockContainer implements IPeripheralProvider
{
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    protected static final AxisAlignedBB AABB_NS = new AxisAlignedBB(0.3125D, 0.0D, 0.125D, 0.6875D, 0.3125D, 0.875D);
    protected static final AxisAlignedBB AABB_WE = new AxisAlignedBB(0.125D, 0.0D, 0.3125D, 0.875D, 0.3125D, 0.6875D);

    public BlockPosTerminal(Material material)
    {
        super(material);
        this.setHardness(0.7f);
        this.setSoundType(SoundType.METAL);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
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
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing facing = EnumFacing.byHorizontalIndex(meta);
        return this.getDefaultState().withProperty(FACING, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int facing = state.getValue(FACING).getHorizontalIndex();
        return facing;
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rotation)
    {
        return state.withProperty(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirror)
    {
        return state.withRotation(mirror.toRotation(state.getValue(FACING)));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        EnumFacing facing = state.getValue(FACING);
        if (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) return AABB_NS;
        return AABB_WE;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.CENTER_SMALL;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityPosTerminal();
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos.down());
        return (state.isTopSolid() || state.getBlockFaceShape(world, pos.down(), EnumFacing.UP) == BlockFaceShape.SOLID) && super.canPlaceBlockAt(world, pos);
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
        IItemHandler itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        return ItemHandlerHelper.calcRedstoneFromInventory(itemHandler);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
            TileEntity tileentity = world.getTileEntity(pos);
            if (tileentity instanceof TileEntityPosTerminal)
            {
                TileEntityPosTerminal tileentityPosTerminal = (TileEntityPosTerminal) tileentity;
                ItemStack stack = player.getHeldItem(hand);
                if (stack.getItem() instanceof IItemCard)
                {
                    tileentityPosTerminal.swipeCard(player, stack);
                    player.addStat(ModStats.CARD_SWIPE);
                }
                else
                {
                    player.openGui(MagneticsMod.instance, ModGuiHandler.GUI_POS_TERMINAL, world, pos.getX(), pos.getY(), pos.getZ());
                    player.addStat(ModStats.POS_TERMINAL_INTERACTION);
                }
            }
        }
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        IItemHandler itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
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

    @Override
    @Optional.Method(modid = CommonProxy.CC_MODID)
    public IPeripheral getPeripheral(World world, BlockPos pos, EnumFacing side)
    {
        TileEntity tileentity = world.getTileEntity(pos);
        return (tileentity instanceof IPeripheral) ? (IPeripheral) tileentity : null;
    }
}
