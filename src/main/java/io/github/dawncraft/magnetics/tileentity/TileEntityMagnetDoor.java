package io.github.dawncraft.magnetics.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

/**
 * Magnet door's tile entity
 *
 * @author QingChenW
 */
public class TileEntityMagnetDoor extends TileEntity
{
    private String UUID;

    @Override
    public boolean shouldRefresh(World world, BlockPos blockPos, IBlockState oldState, IBlockState newState)
    {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        if (!StringUtils.isNullOrEmpty(this.UUID))
            compound.setString("UUID", this.UUID);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        if (compound.hasKey("UUID", Constants.NBT.TAG_STRING))
            this.UUID = compound.getString("UUID");
    }

    public boolean isLocked()
    {
        return !StringUtils.isNullOrEmpty(this.getUUID());
    }

    public String getUUID()
    {
        return this.UUID;
    }

    public void setUUID(String uuid)
    {
        this.UUID = uuid;
    }

    public void resetUUID()
    {
        this.setUUID(null);
    }
}
