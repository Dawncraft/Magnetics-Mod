package io.github.dawncraft.magnetics.sound;

import io.github.dawncraft.magnetics.MagneticsMod;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.GameData;

/**
 * Register mod sounds.
 *
 * @author QingChenW
 */
@Mod.EventBusSubscriber(modid = MagneticsMod.MODID)
public class ModSounds
{
    public static SoundEvent BLOCK_POS_TERMINAL_SWIPE;

    @SubscribeEvent
    public static void onSoundEvenrRegistration(RegistryEvent.Register<SoundEvent> event)
    {
        event.getRegistry().registerAll(
                BLOCK_POS_TERMINAL_SWIPE = createSound("block.pos_terminal.swipe").setRegistryName("block.pos_terminal.swipe")
                );
    }

    private static SoundEvent createSound(String name)
    {
        return new SoundEvent(GameData.checkPrefix(name, true));
    }
}
