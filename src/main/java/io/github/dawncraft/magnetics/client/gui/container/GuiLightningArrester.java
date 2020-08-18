package io.github.dawncraft.magnetics.client.gui.container;

import io.github.dawncraft.magnetics.container.ContainerLightningArrester;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class GuiLightningArrester extends GuiContainer
{
    public GuiLightningArrester(EntityPlayer player, TileEntity tileEntity)
    {
        super(new ContainerLightningArrester(player, tileEntity));

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        // TODO Auto-generated method stub

    }

}
