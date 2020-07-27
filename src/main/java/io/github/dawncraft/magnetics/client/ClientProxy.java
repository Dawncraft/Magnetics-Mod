package io.github.dawncraft.magnetics.client;

import io.github.dawncraft.magnetics.CommonProxy;
import io.github.dawncraft.magnetics.client.renderer.entity.ModEntityRenderers;
import io.github.dawncraft.magnetics.client.renderer.tileentity.ModTileEntityRenderers;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
        ModEntityRenderers.init();
        ModTileEntityRenderers.init();
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);
    }
}
