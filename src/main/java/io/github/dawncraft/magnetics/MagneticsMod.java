package io.github.dawncraft.magnetics;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

/**
 * Magnetics Mod For Minecraft with Forge Mod Loader.
 *
 * @version forge-1.12.2
 * @author QingChenW
 **/
@Mod(modid = MagneticsMod.MODID, name = MagneticsMod.NAME, version = MagneticsMod.VERSION, guiFactory = MagneticsMod.GUI_FACTORY, certificateFingerprint = "")
public class MagneticsMod
{
    public static final String MODID = "magnetics";
    public static final String NAME = "Magnetics Mod";
    public static final String VERSION = "@version@";
    public static final String GUI_FACTORY = "io.github.dawncraft.magnetics.client.GuiFactory";

    @Instance(MagneticsMod.MODID)
    public static MagneticsMod instance;
    @SidedProxy(clientSide = "io.github.dawncraft.magnetics.client.ClientProxy", serverSide = "io.github.dawncraft.magnetics.CommonProxy")
    public static CommonProxy proxy;

    private static Logger logger;

    public static boolean isJEILoaded;
    public static boolean isIC2Loaded;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        isJEILoaded = Loader.isModLoaded("jei");
        if (isJEILoaded) logger.info("JEI was detected, enabling JEI integration.");
        isIC2Loaded = Loader.isModLoaded("ic2");
        if (isIC2Loaded) logger.info("IC2 was detected, enabling the feature.");
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        proxy.serverStarting(event);
    }

    @EventHandler
    public void interModComms(IMCEvent event)
    {
        proxy.interModComms(event);
    }

    @EventHandler
    public void fingerprintViolation(FMLFingerprintViolationEvent event)
    {
        System.out.println("磁学Mod签名校验尚未实现!");
    }

    public static Logger logger()
    {
        return logger;
    }
}
