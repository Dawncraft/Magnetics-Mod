package io.github.dawncraft.magnetics.tileentity;

import java.util.List;

import io.github.dawncraft.magnetics.MagneticsMod;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * Magnet chest's tileentity
 *
 * @author QingChenW
 **/
public class TileEntityMagnetChest extends TileEntityChest
{
    public TileEntityMagnetChest() {}

    public boolean insertStackFromEntity(EntityItem entityItem)
    {
        if (entityItem == null || entityItem.isDead) return false;
        else
        {
            ItemStack itemStack = entityItem.getItem().copy();
            ItemStack newItemStack = this.insertStack(itemStack);

            if (newItemStack.isEmpty())
            {
                entityItem.setDead();
                return true;
            }
            
            entityItem.setItem(newItemStack);
            return false;
        }
    }

    public ItemStack insertStack(ItemStack itemStack)
    {
        int j = this.getSizeInventory();

        for (int k = 0; k < j && !itemStack.isEmpty(); ++k)
            itemStack = this.tryInsertStackToSlot(itemStack, k);

        if (itemStack.isEmpty())
            itemStack = ItemStack.EMPTY;

        return itemStack;
    }

    public ItemStack tryInsertStackToSlot(ItemStack itemStack, int slot)
    {
        ItemStack slotStack = this.getStackInSlot(slot);

        if (this.isItemValidForSlot(slot, itemStack))
        {
            boolean changed = false;

            if (slotStack.isEmpty())
            {
                int max = Math.min(itemStack.getMaxStackSize(), this.getInventoryStackLimit());
                if (itemStack.getCount() <= max)
                {
                    this.setInventorySlotContents(slot, itemStack);
                    itemStack = ItemStack.EMPTY;
                }
                else
                    this.setInventorySlotContents(slot, itemStack.splitStack(max));
                changed = true;
            }
            else if (ItemStack.areItemsEqual(slotStack, itemStack) && ItemStack.areItemStackTagsEqual(slotStack, itemStack))
            {
                int max = Math.min(itemStack.getMaxStackSize(), this.getInventoryStackLimit());
                if (slotStack.getCount() < max)
                {
                    int l = Math.min(itemStack.getCount(), max - slotStack.getCount());
                    itemStack.shrink(1);
                    slotStack.grow(1);
                    changed = l > 0;
                }
            }

            if (changed)
                this.markDirty();
        }
        return itemStack;
    }
    
    @Override
    public boolean shouldRefresh(World world, BlockPos blockPos, IBlockState oldState, IBlockState newState)
    {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public void update()
    {
        super.update();

        this.pullItemsIn();
    }

    /**
     * Come from Trapcraft(<a>https://github.com/percivalalb/Trapcraft</a>)
     * 
     * @author ProPercivalalb
     */
    public void pullItemsIn()
    {
        double centreX = this.pos.getX() + 0.5D;
        double centreY = this.pos.getY() + 0.5D;
        double centreZ = this.pos.getZ() + 0.5D;

        List<EntityItem> entities = this.world.getEntities(EntityItem.class, item -> item.getDistanceSq(centreX, centreY, centreZ) < 16D);

        for (EntityItem itemEntity : entities)
        {
            double diffX = -itemEntity.posX + centreX;
            double diffY = -itemEntity.posY + centreY;
            double diffZ = -itemEntity.posZ + centreZ;
            double speedMultiper = 0.05D;
            double d11 = itemEntity.posX - centreX;
            double d12 = itemEntity.posZ - centreZ;
            double horizDiffSq = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
            double angle = Math.asin(diffX / horizDiffSq);
            double d15 = Math.abs(MathHelper.sin((float) angle) * speedMultiper);
            double d16 = Math.abs(MathHelper.cos((float) angle) * speedMultiper);
            d15 = diffX <= 0.0D ? -d15 : d15;
            d16 = diffZ <= 0.0D ? -d16 : d16;
            if (MathHelper.abs((float) (itemEntity.motionX + itemEntity.motionY + itemEntity.motionZ)) >= 0.2D)
                continue;

            itemEntity.motionX = d15;
            itemEntity.motionY = diffY >= 0.7 ? speedMultiper * 2 : itemEntity.motionY;
            itemEntity.motionZ = d16;
        }
    }

    @Override
    public String getName()
    {
        return this.hasCustomName() ? super.getName() : "container.magnetChest";
    }

    @Override
    public String getGuiID()
    {
        return MagneticsMod.MODID + ":" + "magnet_chest";
    }
}
