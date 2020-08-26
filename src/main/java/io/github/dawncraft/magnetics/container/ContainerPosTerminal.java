package io.github.dawncraft.magnetics.container;

import io.github.dawncraft.magnetics.tileentity.TileEntityPosTerminal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * POS terminal container
 *
 * @author QingChenW
 */
public class ContainerPosTerminal extends Container
{
    private final TileEntityPosTerminal tileentityPosTerminal;

    public ContainerPosTerminal(InventoryPlayer playerInv, TileEntity tileEntity)
    {
        this.tileentityPosTerminal = (TileEntityPosTerminal) tileEntity;
        IItemHandler itemHander = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        this.addSlotToContainer(new SlotItemHandler(itemHander, 0, 17, 8));
        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(playerInv, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        if (this.tileentityPosTerminal.getWorld().getTileEntity(this.tileentityPosTerminal.getPos()) == this.tileentityPosTerminal)
        {
            return player.getDistanceSq(this.tileentityPosTerminal.getPos().add(0.5D, 0.5D, 0.5D)) <= 64.0D;
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

    public TileEntityPosTerminal getTileentityPosTerminal()
    {
        return this.tileentityPosTerminal;
    }
}
