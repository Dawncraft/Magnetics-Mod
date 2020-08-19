package io.github.dawncraft.magnetics.tileentity;

import io.github.dawncraft.magnetics.item.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IWorldNameable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Lightning arrester's tile entity
 *
 * @author QingChenW
 */
public class TileEntityLightningArrester extends TileEntity implements IWorldNameable
{
    private String customName;
    private final ItemStackHandler inventory = new ItemStackHandler()
    {
        @Override
        public boolean isItemValid(int slot, ItemStack stack)
        {
            return stack.getItem() == Items.IRON_INGOT || stack.getItem() == ModItems.MAGNET_SWORD || stack.getItem() == ModItems.MAGNET_WAND;
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
        if (stack.getItem() == Items.IRON_INGOT)
        {
            this.inventory.setStackInSlot(0, new ItemStack(ModItems.MAGNET_INGOT));
        }
        else if (stack.getItem() == ModItems.MAGNET_SWORD || stack.getItem() == ModItems.MAGNET_WAND)
        {
            if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
            NBTTagCompound nbt = stack.getTagCompound();
            nbt.setBoolean("isPowered", true);
        }
    }

    @Override
    public boolean hasCustomName()
    {
        return this.customName != null && !this.customName.isEmpty();
    }

    public void setCustomName(String name)
    {
        this.customName = name;
    }

    @Override
    public String getName()
    {
        return this.hasCustomName() ? this.customName : "container.lightningArrester";
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName());
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return facing != EnumFacing.DOWN;
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
