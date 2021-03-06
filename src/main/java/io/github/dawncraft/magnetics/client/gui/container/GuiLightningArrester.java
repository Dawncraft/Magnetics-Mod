package io.github.dawncraft.magnetics.client.gui.container;

import io.github.dawncraft.magnetics.MagneticsMod;
import io.github.dawncraft.magnetics.container.ContainerLightningArrester;
import io.github.dawncraft.magnetics.tileentity.TileEntityLightningArrester;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Lightning arrester gui
 *
 * @author QingChenW
 */
@SideOnly(Side.CLIENT)
public class GuiLightningArrester extends GuiContainer
{
    private static final ResourceLocation LIGHTNING_ARRESTER_GUI_TEXTURES = new ResourceLocation(MagneticsMod.MODID, "textures/gui/lightning_arrester.png");

    private final InventoryPlayer playerInventory;
    private final TileEntityLightningArrester tileentityLightningArrester;

    public GuiLightningArrester(InventoryPlayer playerInv, TileEntity tileEntity)
    {
        super(new ContainerLightningArrester(playerInv, tileEntity));
        this.playerInventory = playerInv;
        this.tileentityLightningArrester = (TileEntityLightningArrester) tileEntity;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String s = this.tileentityLightningArrester.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 0x404040);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(LIGHTNING_ARRESTER_GUI_TEXTURES);
        int x = (this.width - this.xSize) / 2, y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }
}
