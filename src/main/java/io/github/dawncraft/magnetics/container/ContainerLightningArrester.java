package io.github.dawncraft.magnetics.container;

import io.github.dawncraft.magnetics.tileentity.TileEntityLightningArrester;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerLightningArrester extends Container
{
    private final TileEntityLightningArrester tileentityLightningArrester;

    public ContainerLightningArrester(EntityPlayer player, TileEntity tileEntity)
    {
        this.tileentityLightningArrester = (TileEntityLightningArrester) tileEntity;
        IItemHandler itemHander = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
        this.addSlotToContainer(new SlotItemHandler(itemHander, 0, 56, 26));
        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        if (this.tileentityLightningArrester.getWorld().getTileEntity(this.tileentityLightningArrester.getPos()) == this.tileentityLightningArrester)
        {
            return player.getDistanceSq(this.tileentityLightningArrester.getPos().add(0.5D, 0.5D, 0.5D)) <= 64.0D;
        }
        return false;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index)
    {
        ItemStack oldItemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack newItemstack = slot.getStack();
            oldItemstack = newItemstack.copy();

            Boolean isMerged = false;
            if (index == 0)
            {
                isMerged = this.mergeItemStack(newItemstack, 1, 37, true);
            }
            else if (index >= 1 && index < 28)
            {
                isMerged = this.mergeItemStack(newItemstack, 0, 1, false) || this.mergeItemStack(newItemstack, 28, 37, false);
            }
            else if (index >= 28 && index < 37)
            {
                isMerged = this.mergeItemStack(newItemstack, 0, 1, false) || this.mergeItemStack(newItemstack, 1, 28, false);
            }
            if (!isMerged) return ItemStack.EMPTY;

            if (newItemstack.isEmpty()) slot.putStack(ItemStack.EMPTY);
            else slot.onSlotChanged();

            slot.onTake(player, newItemstack);
        }
        return oldItemstack;
    }
}
