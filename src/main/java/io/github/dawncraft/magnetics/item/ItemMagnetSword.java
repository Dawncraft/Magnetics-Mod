package io.github.dawncraft.magnetics.item;

import io.github.dawncraft.magnetics.util.Utils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Magnet sword
 *
 * @author QingChenW
 */
public class ItemMagnetSword extends ItemSword
{
    public ItemMagnetSword(ToolMaterial material)
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
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        super.hitEntity(stack, target, attacker);
        if (this.isPowered(stack))
        {
            Utils.summonLightningBoltAt(attacker, target.world, target.posX, target.posY, target.posZ);
        }
        return true;
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
