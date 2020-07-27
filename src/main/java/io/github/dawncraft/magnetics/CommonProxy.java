package io.github.dawncraft.magnetics;

import io.github.dawncraft.magnetics.item.ModOreDictionary;
import io.github.dawncraft.magnetics.tileentity.ModTileEntities;
import io.github.dawncraft.magnetics.world.gen.feature.ModWorldGenerators;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class CommonProxy
{
    public void preInit(FMLPreInitializationEvent event)
    {
        ConfigLoader.init(event.getSuggestedConfigurationFile());
        ModTileEntities.init();
    }

    public void init(FMLInitializationEvent event)
    {
        ModOreDictionary.init();
        ModWorldGenerators.init();
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
