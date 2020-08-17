package io.github.dawncraft.magnetics.item;

import java.util.List;

import javax.annotation.Nullable;

import io.github.dawncraft.magnetics.block.ModBlocks;
import io.github.dawncraft.magnetics.tileentity.TileEntityMagnetDoor;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Magnet door
 *
 * @author QingChenW
 */
public class ItemMagnetDoor extends ItemDoor
{
    public ItemMagnetDoor()
    {
        super(ModBlocks.MAGNET_DOOR);
    }
    
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = player.getHeldItem(hand);
        NBTTagCompound nbt = stack.hasTagCompound() ? stack.getTagCompound() : null;
        
        if (super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ) == EnumActionResult.SUCCESS)
        {
            IBlockState blockState = world.getBlockState(pos);
            Block block = blockState.getBlock();
            if (block != ModBlocks.MAGNET_DOOR) pos = pos.offset(facing);
            
            TileEntity tileentity = world.getTileEntity(pos);
            if (tileentity instanceof TileEntityMagnetDoor)
            {
                TileEntityMagnetDoor tileentityMagnetDoor = (TileEntityMagnetDoor) tileentity;
                if (nbt.hasKey("UUID", Constants.NBT.TAG_STRING))
                {
                    tileentityMagnetDoor.setUUID(nbt.getString("UUID"));
                }
                else if (nbt.hasKey("BlockEntityTag", Constants.NBT.TAG_COMPOUND))
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                }
            }
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
    {
        if (stack.hasTagCompound())
        {
            NBTTagCompound nbt = stack.getTagCompound();
            String name = nbt.getString("UUID");
            if (!StringUtils.isNullOrEmpty(name))
            {
                tooltip.add(TextFormatting.GRAY + I18n.format(this.getTranslationKey() + ".desc", name));
            }
        }
    }
}
