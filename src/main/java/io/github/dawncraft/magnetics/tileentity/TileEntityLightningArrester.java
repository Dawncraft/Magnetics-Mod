package io.github.dawncraft.magnetics.tileentity;

import io.github.dawncraft.magnetics.item.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 * TODO 避雷针te的update及界面以及同步未实现
 *
 * @author QingChenW
 */
public class TileEntityLightningArrester extends TileEntity
{
    private final ItemStackHandler inventory = new ItemStackHandler()
    {
        @Override
        public boolean isItemValid(int slot, ItemStack stack)
        {
            return stack.getItem() == ModItems.MAGNET_SWORD || stack.getItem() == ModItems.MAGNET_WAND;
        }

        @Override
        public int getSlotLimit(int slot)
        {
            return 1;
        }
    };

    public void lightningStrike()
    {
        ItemStack stack = this.inventory.getStackInSlot(0);
        if (stack.getItem() == ModItems.MAGNET_SWORD || stack.getItem() == ModItems.MAGNET_WAND)
        {
            if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
            NBTTagCompound nbt = stack.getTagCompound();
            nbt.setBoolean("isPowered", true);
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return facing == EnumFacing.UP;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.inventory);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        this.inventory.deserializeNBT(tag.getCompoundTag("Inventory"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        tag.setTag("Inventory", this.inventory.serializeNBT());
        return super.writeToNBT(tag);
    }
}
