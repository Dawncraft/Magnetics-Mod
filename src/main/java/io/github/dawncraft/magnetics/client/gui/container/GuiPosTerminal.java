package io.github.dawncraft.magnetics.client.gui.container;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import io.github.dawncraft.magnetics.MagneticsMod;
import io.github.dawncraft.magnetics.api.item.IItemCard;
import io.github.dawncraft.magnetics.container.ContainerPosTerminal;
import io.github.dawncraft.magnetics.tileentity.TileEntityPosTerminal;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * POS terminal gui
 *
 * @author QingChenW
 */
@SideOnly(Side.CLIENT)
public class GuiPosTerminal extends GuiContainer
{
    private static final ResourceLocation POS_TERMINAL_GUI_TEXTURES = new ResourceLocation(MagneticsMod.MODID, "textures/gui/pos_terminal.png");

    private final InventoryPlayer playerInventory;
    private final TileEntityPosTerminal tileentityPosTerminal;

    private GuiTextField keyField;
    private GuiTextField valueField;
    private GuiButton readButton;
    private GuiButton writeButton;
    private GuiButton linkButton;

    public GuiPosTerminal(InventoryPlayer playerInv, TileEntity tileEntity)
    {
        super(new ContainerPosTerminal(playerInv, tileEntity));
        this.playerInventory = playerInv;
        this.tileentityPosTerminal = (TileEntityPosTerminal) tileEntity;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        this.keyField = new GuiTextField(0, this.fontRenderer, 52, 16, 50, 12);
        this.valueField = new GuiTextField(1, this.fontRenderer, 112, 16, 50, 12);
        this.buttonList.clear();
        this.readButton = this.addButton(new GuiButton(2, 52 + this.guiLeft, 32 + this.guiTop, 50, 12, "Read"));
        this.writeButton = this.addButton(new GuiButton(3, 112 + this.guiLeft, 32 + this.guiTop, 50, 12, "Write"));
        this.linkButton = this.addButton(new GuiButton(4, 52 + this.guiLeft, 48 + this.guiTop, 100, 40, "Link"));
        this.readButton.enabled = this.writeButton.enabled = false;
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
        String s = this.tileentityPosTerminal.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(s, 52, 6, 0x404040);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 0x404040);

        this.keyField.drawTextBox();
        this.valueField.drawTextBox();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(POS_TERMINAL_GUI_TEXTURES);
        int x = (this.width - this.xSize) / 2, y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
            // TODO 全得改成发包

            if (button.id == 2)
            {
                String key = this.keyField.getText();
                if (key.isEmpty()) return;

                ItemStack stack = this.tileentityPosTerminal.getCard();
                if (stack.getItem() instanceof IItemCard)
                {
                    IItemCard itemCard = (IItemCard) stack.getItem();
                    String value = itemCard.getData(stack, key);
                    if (!StringUtils.isNullOrEmpty(value)) this.valueField.setText(value);
                }
            }
            else if (button.id == 3)
            {
                String key = this.keyField.getText();
                String value = this.valueField.getText();
                if (key.isEmpty()) return;

                ItemStack stack = this.tileentityPosTerminal.getCard();
                if (stack.getItem() instanceof IItemCard)
                {
                    IItemCard itemCard = (IItemCard) stack.getItem();
                    try
                    {
                        itemCard.setData(stack, key, value);
                    }
                    catch (IItemCard.WriteException e)
                    {
                        this.valueField.setText("Error: " + e.reason);
                        return;
                    }
                }
            }
            else if (button.id == 4)
            {

            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.keyField.mouseClicked(mouseX - this.guiLeft, mouseY - this.guiTop, mouseButton);
        this.valueField.mouseClicked(mouseX - this.guiLeft, mouseY - this.guiTop, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        boolean handled = this.keyField.textboxKeyTyped(typedChar, keyCode) || this.valueField.textboxKeyTyped(typedChar, keyCode);
        this.readButton.enabled = this.writeButton.enabled = !this.keyField.getText().trim().isEmpty();
        if (handled) return;
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        this.keyField.updateCursorCounter();
        this.valueField.updateCursorCounter();
    }

    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }
}
