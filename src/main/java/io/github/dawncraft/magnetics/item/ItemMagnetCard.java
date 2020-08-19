package io.github.dawncraft.magnetics.item;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A magnet card that can generate a random id and also can be written by other mods.
 *
 * @author QingChenW
 */
public class ItemMagnetCard extends Item
{
    public ItemMagnetCard()
    {
        this.setMaxStackSize(16);
    }

    @Override
    public int getItemStackLimit(ItemStack itemStack)
    {
        return itemStack.hasTagCompound() ? 1 : super.getItemStackLimit(itemStack);
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player)
    {
        return true;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote)
        {
            if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
            NBTTagCompound nbt = stack.getTagCompound();
            if (!nbt.hasKey("UUID", Constants.NBT.TAG_STRING))
            {
                ItemStack newStack = new ItemStack(ModItems.MAGNET_CARD);
                nbt.setString("UUID", UUID.randomUUID().toString());
                nbt.setString("Owner", player.getName());
                nbt.setString("Text", "");
                nbt.setTag("Data", new NBTTagCompound());
                newStack.setTagCompound(nbt);

                stack.shrink(1);
                if (stack.isEmpty())
                    return new ActionResult<>(EnumActionResult.SUCCESS, newStack);
                if (!player.inventory.addItemStackToInventory(newStack.copy()))
                    player.dropItem(newStack, false);

                return new ActionResult<>(EnumActionResult.SUCCESS, stack);
            }
        }
        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    /**
     * 实现了类似原版的HideFlags
     * 1为隐藏卡号
     * 2为隐藏所有者
     * 4为隐藏额外信息
     * 7为全部隐藏
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
    {
        if (stack.hasTagCompound())
        {
            NBTTagCompound nbt = stack.getTagCompound();
            int hideFlags = nbt.hasKey("CardHideFlags", Constants.NBT.TAG_ANY_NUMERIC) ? nbt.getInteger("CardHideFlags") : 0;

            if ((hideFlags & 1) == 0)
            {
                String uuid = nbt.getString("UUID");
                if (!StringUtils.isNullOrEmpty(uuid)) tooltip.add(TextFormatting.GRAY + I18n.format(this.getTranslationKey() + ".desc.id", uuid));
            }
            if ((hideFlags & 2) == 0)
            {
                String name = nbt.getString("Owner");
                if (!StringUtils.isNullOrEmpty(name)) tooltip.add(TextFormatting.GRAY + I18n.format(this.getTranslationKey() + ".desc.name", name));
            }
            if ((hideFlags & 4) == 0)
            {
                String text = nbt.getString("Text");
                if (!StringUtils.isNullOrEmpty(text)) tooltip.add(TextFormatting.GRAY + I18n.format(this.getTranslationKey() + ".desc.text", text));
            }
        }
        else
        {
            tooltip.add(TextFormatting.GRAY + I18n.format(this.getTranslationKey() + ".desc.blank"));
        }
    }
}
