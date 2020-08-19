package io.github.dawncraft.magnetics.item;

import io.github.dawncraft.magnetics.util.Utils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Magnet wand
 *
 * @author QingChenW
 */
public class ItemMagnetWand extends ItemWand
{
    public ItemMagnetWand(ToolMaterial material)
    {
        super(material);
    }

    @Override
    public String getTranslationKey(ItemStack stack)
    {
        return super.getTranslationKey(stack) + (this.isPowered(stack) ? ".powered" : "");
    }

    @Override
    public IRarity getForgeRarity(ItemStack stack)
    {
        if (this.isPowered(stack)) return EnumRarity.EPIC;
        return super.getForgeRarity(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        return this.isPowered(stack) || super.hasEffect(stack);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (this.isInCreativeTab(tab))
        {
            items.add(new ItemStack(this));
            ItemStack stack = new ItemStack(this);
            if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
            NBTTagCompound nbt = stack.getTagCompound();
            nbt.setBoolean("isPowered", true);
            items.add(stack);
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack itemStack = player.getHeldItem(hand);
        if (this.isPowered(itemStack))
        {
            RayTraceResult result = Utils.rayTrace(player, 12.0D);
            if (result != null && result.typeOfHit == Type.ENTITY)
            {
                Entity target = result.entityHit;
                Utils.summonLightningBoltAt(player, target.world, target.posX, target.posY, target.posZ);
                itemStack.damageItem(1, player);
                player.getCooldownTracker().setCooldown(this, 20);
                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);
            }
        }
        return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStack);
    }

    private boolean isPowered(ItemStack stack)
    {
        if (stack.hasTagCompound())
        {
            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt.getBoolean("isPowered")) return true;
        }
        return false;
    }
}
