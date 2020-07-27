package io.github.dawncraft.magnetics.entity;

import io.github.dawncraft.magnetics.MagneticsMod;
import io.github.dawncraft.magnetics.entity.projectile.EntityMagnetBall;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Register some entities.
 *
 * @author QingChenW
 */
@Mod.EventBusSubscriber(modid = MagneticsMod.MODID)
public class ModEntities
{
    private static int nextID = 0;

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event)
    {
    	event.getRegistry().registerAll(EntityEntryBuilder.create().entity(EntityMagnetBall.class).tracker(64, 10, true).name("MagnetBall").id("magnet_ball", nextID++).build());

    }
}
