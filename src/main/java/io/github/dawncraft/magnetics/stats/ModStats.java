package io.github.dawncraft.magnetics.stats;

import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatBasic;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Some stats in the mod
 *
 * @author QingChenW
 */
public class ModStats
{
    public static final StatBase LIGHTNING_ARRESTER_INTERACTION = new StatBasic("stat.lightningArresterInteraction", new TextComponentTranslation("stat.lightningArresterInteraction")).registerStat();
}
