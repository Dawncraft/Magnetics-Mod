package io.github.dawncraft.magnetics.tileentity;

import io.github.dawncraft.magnetics.MagneticsMod;
import io.github.dawncraft.magnetics.api.item.IItemCard;
import io.github.dawncraft.magnetics.sound.ModSounds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityPosTerminal extends TileEntity implements IWorldNameable
{
    private String customName;

    private final ItemStackHandler inventory = new ItemStackHandler()
    {
        @Override
        public boolean isItemValid(int slot, ItemStack stack)
        {
            return stack.getItem() instanceof IItemCard;
        }

        @Override
        public int getSlotLimit(int slot)
        {
            return 1;
        }
    };

    @Override
    public boolean shouldRefresh(World world, BlockPos blockPos, IBlockState oldState, IBlockState newState)
    {
        return oldState.getBlock() != newState.getBlock();
    }

    public void swipeCard(EntityPlayer player, ItemStack stack)
    {
        MagneticsMod.logger().info("Player has swiped a card!!!");
        player.world.playSound(null, this.getPos(), ModSounds.BLOCK_POS_TERMINAL_SWIPE, SoundCategory.BLOCKS, 1.0F, 1.0F);
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
        return this.hasCustomName() ? this.customName : "container.posTerminal";
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName());
    }
}
