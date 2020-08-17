package io.github.dawncraft.magnetics.block;

import java.util.Random;

import io.github.dawncraft.magnetics.item.ModItems;
import io.github.dawncraft.magnetics.tileentity.TileEntityMagnetDoor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Magnet door
 *
 * @author QingChenW
 */
public class BlockMagnetDoor extends BlockDoor implements ITileEntityProvider
{
    public BlockMagnetDoor()
    {
        super(Material.IRON);
        this.hasTileEntity = true;
        this.setHardness(5.0f);
        this.setResistance(10.0f);
        this.setHarvestLevel("pickaxe", 1);
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return MapColor.IRON;
    }
    
    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return super.hasTileEntity(state) && state.getValue(HALF) == BlockDoor.EnumDoorHalf.LOWER;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityMagnetDoor();
    }

    @Override
    public Item getItem()
    {
        return ModItems.MAGNET_DOOR;
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos fromPos)
    {
        if (state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER)
        {
            BlockPos pos2 = pos.down();
            IBlockState state2 = world.getBlockState(pos2);

            if (state2.getBlock() != this)
            {
                world.setBlockToAir(pos);
            }
            else if (neighborBlock != this)
            {
                state2.neighborChanged(world, pos2, neighborBlock, fromPos);
            }
        }
        else
        {
            boolean removed = false;
            BlockPos pos2 = pos.up();
            IBlockState state2 = world.getBlockState(pos2);

            if (state2.getBlock() != this)
            {
            	if (!world.isRemote) this.dropBlockAsItem(world, pos, state, 0);
            	// 这里setBlockState之后tileentity就没了,所以没有采用和mojang一样的写法
                world.setBlockToAir(pos);
            }

            if (!world.getBlockState(pos.down()).isSideSolid(world, pos.down(), EnumFacing.UP))
            {
                if (!world.isRemote) this.dropBlockAsItem(world, pos, state, 0);
                world.setBlockToAir(pos);
                if (state2.getBlock() == this)
                {
                    world.setBlockToAir(pos2);
                }
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote && hand == EnumHand.MAIN_HAND)
        {
            BlockPos newPos = state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER ? pos.down() : pos;
            IBlockState newState = world.getBlockState(newPos);
            if (newState.getBlock() == this)
            {
                boolean canUnlock = true;
                if (true) // TODO 被红石激活
                {
                    canUnlock = false;
                    TileEntity tileentity = world.getTileEntity(newPos);
                    if (tileentity instanceof TileEntityMagnetDoor)
                    {
                        TileEntityMagnetDoor tileentityMagnetDoor = (TileEntityMagnetDoor) tileentity;
                        if (tileentityMagnetDoor.isLocked())
                        {
                            ItemStack itemStack = player.getHeldItem(hand);
                            if (itemStack.getItem() == ModItems.MAGNET_CARD && itemStack.hasTagCompound())
                            {
                                NBTTagCompound nbt = itemStack.getTagCompound();
                                String UUID = nbt.getString("UUID");
                                if (!StringUtils.isNullOrEmpty(UUID))
                                {
                                    if (!player.isSneaking())
                                    {
                                        canUnlock = UUID.equals(tileentityMagnetDoor.getUUID());
                                    }
                                    else
                                    {
                                        tileentityMagnetDoor.resetUUID();
                                        player.sendStatusMessage(new TextComponentTranslation("container.unlock", this.getLocalizedName()), true);
                                        return true;
                                    }
                                }
                            }
                        }
                        else
                        {
                            canUnlock = true;
                            ItemStack itemStack = player.getHeldItem(hand);
                            if (itemStack.getItem() == ModItems.MAGNET_CARD && itemStack.hasTagCompound())
                            {
                                NBTTagCompound nbt = itemStack.getTagCompound();
                                String UUID = nbt.getString("UUID");
                                if (!StringUtils.isNullOrEmpty(UUID))
                                {
                                    if (player.isSneaking())
                                    {
                                        tileentityMagnetDoor.setUUID(UUID);
                                        player.sendStatusMessage(new TextComponentTranslation("container.lock", this.getLocalizedName()), true);
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
                if (canUnlock)
                {
                    state = newState.cycleProperty(OPEN);
                    world.setBlockState(newPos, state, 10);
                    world.markBlockRangeForRenderUpdate(newPos, pos);
                    world.playEvent(null, state.getValue(OPEN).booleanValue() ? this.getOpenSound() : this.getCloseSound(), pos, 0);
                    return true;
                }
                player.sendStatusMessage(new TextComponentTranslation("container.isLocked", this.getLocalizedName()), true);
            }
        }
        return false;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState blockState, int fortune)
    {
        Random rand = world instanceof World ? ((World) world).rand : RANDOM;
        
        Item item = this.getItemDropped(blockState, rand, fortune);
        if (item != Items.AIR)
        {
            ItemStack itemStack = new ItemStack(item);
            if (blockState.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER) pos = pos.down();
            TileEntity tileentity = world.getTileEntity(pos);
            if (tileentity instanceof TileEntityMagnetDoor)
            {
                TileEntityMagnetDoor tileentityMagnetDoor = (TileEntityMagnetDoor) tileentity;
                if (tileentityMagnetDoor.isLocked())
                {
                    NBTTagCompound nbt = new NBTTagCompound();
                    nbt.setString("UUID", tileentityMagnetDoor.getUUID());
                    itemStack.setTagCompound(nbt);
                }
            }
            drops.add(itemStack);
        }
    }
    
    // forge打的中键选取补丁只支持获取在当前指针指着的方块的te,门的上半部分需要自己实现
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
    	ItemStack stack = getItem(world, pos, state);
    	if (state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER)
    	{
    		boolean isCreative = player.capabilities.isCreativeMode;
        	if (isCreative && GuiScreen.isCtrlKeyDown())
        	{
                TileEntity tileentity = world.getTileEntity(pos.down());
                if (tileentity != null)
                {
                	Minecraft.getMinecraft().storeTEInStack(stack, tileentity);
                }
        	}
    	}
    	return stack;
    }

    @Override
    public boolean eventReceived(IBlockState state, World world, BlockPos pos, int id, int param)
    {
        super.eventReceived(state, world, pos, id, param);
        TileEntity tileentity = world.getTileEntity(pos);
        return tileentity != null ? tileentity.receiveClientEvent(id, param) : false;
    }
}
