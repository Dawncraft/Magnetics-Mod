package io.github.dawncraft.magnetics.api.item;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Armor item suit
 *
 * @author QingChenW
 */
public class ItemArmorSuit extends ItemArmor
{
    public ItemArmorSuit(ArmorMaterial material, int renderIndex, EntityEquipmentSlot equipmentSlot)
    {
        super(material, renderIndex, equipmentSlot);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag)
    {
        tooltip.add(TextFormatting.GRAY + I18n.format("item.suit_effect"));
        int count = this.getArmorCount(Minecraft.getMinecraft().player, this.getArmorMaterial());
        TextFormatting formatCode = count < 4 ? TextFormatting.DARK_GRAY : TextFormatting.GREEN;
        tooltip.add(" " + formatCode + I18n.format("item.armor." + this.getArmorMaterial().getName() +".suit_effect", count));
    }

    public static int getArmorCount(EntityPlayer player, ItemArmor.ArmorMaterial material)
    {
        int count = 0;
        if (player != null)
        {
            for (ItemStack stack : player.getArmorInventoryList())
            {
                Item item = stack.getItem();
                if (item instanceof ItemArmor && ((ItemArmor) item).getArmorMaterial() == material)
                {
                    ++count;
                }
            }
        }
        return count;
    }
}
