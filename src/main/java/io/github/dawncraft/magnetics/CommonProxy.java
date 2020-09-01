package io.github.dawncraft.magnetics;

import io.github.dawncraft.magnetics.container.ModGuiHandler;
import io.github.dawncraft.magnetics.item.ModOreDictionary;
import io.github.dawncraft.magnetics.network.ModNetworkManager;
import io.github.dawncraft.magnetics.recipe.ModRecipes;
import io.github.dawncraft.magnetics.world.gen.feature.ModWorldGenerators;
import net.minecraftforge.fml.common.Loader;
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
    public static final String JEI_MODID = "jei";
    public static final String IC2_MODID = "ic2";
    public static final String CC_MODID = "computercraft";
    public static final String OC_MODID = "opencomputers";

    public static boolean isJEILoaded;
    public static boolean isIC2Loaded;
    public static boolean isCCLoaded;
    public static boolean isOCLoaded;

    public void preInit(FMLPreInitializationEvent event)
    {
        ConfigLoader.init(event.getSuggestedConfigurationFile());

        isJEILoaded = checkModExist(JEI_MODID);
        isIC2Loaded = checkModExist(IC2_MODID);
        isCCLoaded = checkModExist(CC_MODID);
        isOCLoaded = checkModExist(OC_MODID);
    }

    public void init(FMLInitializationEvent event)
    {
        ModOreDictionary.init();
        ModRecipes.init();
        ModWorldGenerators.init();

        ModNetworkManager.init();
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

    private static boolean checkModExist(String modid)
    {
        boolean isLoaded = Loader.isModLoaded(modid);
        if (isLoaded)
        {
            String name = Loader.instance().getIndexedModList().get(modid).getName();
            MagneticsMod.logger().info(name + " was detected, enabling the feature.");
        }
        return isLoaded;
    }
}
