package io.github.dawncraft.magnetics.item;

import java.util.UUID;

import com.google.common.collect.Multimap;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.AttributeModifierOperation;
import net.minecraftforge.oredict.OreDictionary;

/**
 * A new weapon that can spell skills.
 * <p>
 * Come form Dawncraft Mod
 *
 * @author QingChenW
 */
public class ItemWand extends Item
{
    private final Item.ToolMaterial material;

    public ItemWand(Item.ToolMaterial material)
    {
        this.material = material;
        this.setFull3D();
        this.setMaxStackSize(1);
        this.setMaxDamage(material.getMaxUses());
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        stack.damageItem(2, attacker);
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
    {
        if (state.getBlockHardness(world, pos) != 0.0D)
        {
            stack.damageItem(2, entityLiving);
        }
        return true;
    }

    @Override
    public int getItemEnchantability()
    {
        return this.material.getEnchantability();
    }

    public String getToolMaterialName()
    {
        return this.material.toString();
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        ItemStack mat = this.material.getRepairItemStack();
        if (mat != null && OreDictionary.itemMatches(mat, repair, false)) return true;
        return super.getIsRepairable(toRepair, repair);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
    {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
        if (slot == EntityEquipmentSlot.MAINHAND)
        {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.material.getAttackDamage(), AttributeModifierOperation.ADD));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -3.2D, AttributeModifierOperation.ADD));
        }
        return multimap;
    }
}
