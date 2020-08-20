package io.github.dawncraft.magnetics;

import io.github.dawncraft.magnetics.container.ModGuiHandler;
import io.github.dawncraft.magnetics.item.ModOreDictionary;
import io.github.dawncraft.magnetics.recipe.ModRecipes;
import io.github.dawncraft.magnetics.world.gen.feature.ModWorldGenerators;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

/**
 * The common proxy of mod.
 *
 * @author QingChenW
 */
public class CommonProxy
{
    public void preInit(FMLPreInitializationEvent event)
    {
        ConfigLoader.init(event.getSuggestedConfigurationFile());
    }

    public void init(FMLInitializationEvent event)
    {
        ModOreDictionary.init();
        ModRecipes.init();
        ModWorldGenerators.init();
        ModGuiHandler.init();
    }

    public void postInit(FMLPostInitializationEvent event)
    {

    }

    public void serverStarting(FMLServerStartingEvent event)
    {

    }

    public void interModComms(IMCEvent event)
    {

    }
}
