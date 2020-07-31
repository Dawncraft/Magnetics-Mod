package io.github.dawncraft.magnetics.client;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import io.github.dawncraft.magnetics.ConfigLoader;
import io.github.dawncraft.magnetics.MagneticsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

/**
 * A factory which provides the mod's config screen
 *
 * @author QingChenW
 */
public class GuiFactory implements IModGuiFactory
{
    @Override
    public void initialize(Minecraft minecraft) {}

    @Override
    public boolean hasConfigGui()
    {
        return true;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parent)
    {
        return new GuiConfig(parent, ConfigElement.from(ConfigLoader.class).getChildElements(), MagneticsMod.MODID, false, false, MagneticsMod.NAME);
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
    {
        return Collections.emptySet();
    }
}
