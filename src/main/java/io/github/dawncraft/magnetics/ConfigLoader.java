package io.github.dawncraft.magnetics;

import java.io.File;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Register a configuration manager and some configurations.
 *
 * @author QingChenW
 */
@Mod.EventBusSubscriber(modid = MagneticsMod.MODID)
@Config(modid = MagneticsMod.MODID)
public class ConfigLoader
{
    @Config.Ignore
    private static Configuration config;

    @Config.Comment("Whether use separate creative tab for mod items or not.")
    @Config.LangKey("config." + MagneticsMod.MODID + ".useSeparateCreativeTab")
    public static boolean useSeparateCreativeTab = true;

    public static void init(File file)
    {
        config = new Configuration(file);
        config.load();
        MagneticsMod.logger().info("Load configuration successfully.");
    }

    @SubscribeEvent
    public static void onConfigChanged(OnConfigChangedEvent event)
    {
        if (event.getModID().equals(MagneticsMod.MODID))
        {
            ConfigManager.sync(MagneticsMod.MODID, Config.Type.INSTANCE);
        }
    }

    public static Configuration config()
    {
        return config;
    }
}
