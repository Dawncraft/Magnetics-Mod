package io.github.dawncraft.magnetics.client.gui.container;

import java.io.IOException;
import java.util.List;

import org.lwjgl.input.Keyboard;

import io.github.dawncraft.magnetics.MagneticsMod;
import io.github.dawncraft.magnetics.api.item.IItemCard;
import io.github.dawncraft.magnetics.container.ContainerPosTerminal;
import io.github.dawncraft.magnetics.network.MessageWriteCard;
import io.github.dawncraft.magnetics.network.ModNetworkManager;
import io.github.dawncraft.magnetics.tileentity.TileEntityPosTerminal;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
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

    private ItemStack lastCard = ItemStack.EMPTY;

    private String info;
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
        this.info = "Ready.";
        this.keyField = new GuiTextField(0, this.fontRenderer, 52, 16, 50, 12);
        this.valueField = new GuiTextField(1, this.fontRenderer, 112, 16, 50, 12);
        this.buttonList.clear();
        this.readButton = this.addButton(new GuiButton(2, 52 + this.guiLeft, 32 + this.guiTop, 50, 12, "Read"));
        this.writeButton = this.addButton(new GuiButton(3, 112 + this.guiLeft, 32 + this.guiTop, 50, 12, "Write"));
        this.linkButton = this.addButton(new GuiButton(4, 52 + this.guiLeft, 48 + this.guiTop, 100, 20, "Link"));
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
        this.fontRenderer.drawString(this.info, 82, 6, 0x404040);

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
            if (button.id == 2)
            {
                String key = this.keyField.getText();
                if (key.isEmpty()) return;
                this.valueField.setText(MessageWriteCard.MessageWriteCardHandler.readCard(this.tileentityPosTerminal, key));
                this.info = "read";
            }
            else if (button.id == 3)
            {
                String key = this.keyField.getText();
                String value = this.valueField.getText();
                if (key.isEmpty()) return;
                try
                {
                    ModNetworkManager.sendMessageToServer(MessageWriteCard.createWriteMessage(key, value));
                    MessageWriteCard.MessageWriteCardHandler.writeCard(this.tileentityPosTerminal, key, value);
                    this.info = "write";
                }
                catch (IItemCard.WriteException e)
                {
                    this.info = "Error: " + e.reason;
                }
            }
            else if (button.id == 4)
            {
                List<String> list = this.tileentityPosTerminal.getComputerList();
                if (!list.isEmpty())
                {
                    String id = list.get(0);
                    ModNetworkManager.sendMessageToServer(MessageWriteCard.createConnectMessage(id));
                    this.info = "Try to connect to " + id;
                }
                else
                {
                    this.info = "No computer connected!";
                }
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

        if (!ItemStack.areItemStacksEqual(this.lastCard, this.tileentityPosTerminal.getLastCard()))
        {
            this.lastCard = this.tileentityPosTerminal.getLastCard();
            if (this.lastCard.getItem() instanceof IItemCard)
            {
                IItemCard itemCard = (IItemCard) this.lastCard.getItem();
                this.info = "Last: " + itemCard.getData(this.lastCard, "Owner") + "(" + itemCard.getData(this.lastCard, "UUID").substring(0, 6) + "*)";
            }
        }

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
