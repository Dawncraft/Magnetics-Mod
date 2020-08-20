package io.github.dawncraft.magnetics.potion;

import javax.annotation.Nullable;

import io.github.dawncraft.magnetics.MagneticsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 从Dawncraft Mod复制过来的, 也许没啥用了
 *
 * @author QingChenW
 */
@Deprecated
public class PotionMod extends Potion
{
    private static final ResourceLocation POTION_TEXTURE = new ResourceLocation(MagneticsMod.MODID + ":" + "textures/gui/potion.png");

    public PotionMod(boolean isBadEffect, int liquidColor)
    {
        super(isBadEffect, liquidColor);
    }

    @Override
    public boolean isInstant()
    {
        return false;
    }

    @Override
    public boolean isReady(int duration, int amplifier)
    {
        return false;
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier)
    {
    }

    @Override
    public void affectEntity(@Nullable Entity source, @Nullable Entity indirectSource, EntityLivingBase entityLivingBase, int amplifier, double health)
    {
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderInventoryEffect(PotionEffect effect, Gui gui, int x, int y, float z)
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.getPotionTexture(effect));
        gui.drawTexturedModalRect(x + 6, y + 7, 0, 0, 16, 16);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderHUDEffect(PotionEffect effect, Gui gui, int x, int y, float z, float alpha)
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.getPotionTexture(effect));
        gui.drawTexturedModalRect(x + 4, y + 4, 0, 0, 16, 16);
    }

    @SideOnly(Side.CLIENT)
    public ResourceLocation getPotionTexture(PotionEffect effect)
    {
        return POTION_TEXTURE;
    }
}
