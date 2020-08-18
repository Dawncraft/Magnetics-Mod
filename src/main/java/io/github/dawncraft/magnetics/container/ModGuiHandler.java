package io.github.dawncraft.magnetics.container;

import io.github.dawncraft.magnetics.MagneticsMod;
import io.github.dawncraft.magnetics.client.gui.container.GuiLightningArrester;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModGuiHandler implements IGuiHandler
{
    public static final int GUI_LIGHTNING_ARRESTER = 1;

    public static void init()
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(MagneticsMod.instance, new ModGuiHandler());
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        switch (ID)
        {
        case GUI_LIGHTNING_ARRESTER: return new ContainerLightningArrester(player, player.world.getTileEntity(new BlockPos(x, y, z)));
        }
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        switch (ID)
        {
        case GUI_LIGHTNING_ARRESTER: return new GuiLightningArrester(player, player.world.getTileEntity(new BlockPos(x, y, z)));
        }
        return null;
    }
}
