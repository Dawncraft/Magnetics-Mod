package io.github.dawncraft.magnetics.potion;

import io.github.dawncraft.magnetics.MagneticsMod;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Register some potions.
 *
 * @author QingChenW
 */
@Mod.EventBusSubscriber(modid = MagneticsMod.MODID)
public class ModPotions
{
    public static Potion PARALYSIS;

    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> event)
    {
        event.getRegistry().registerAll(PARALYSIS = new PotionMod(true, 0x3C64C8).setPotionName("potion.paralysis").registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "D7E4C176-9516-6FB0-1099-1640D8327AC3", -0.25D, Constants.AttributeModifierOperation.MULTIPLY).registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE, "A6FC931F-16EE-4097-98C8-3DF2910073EB", -3.0D, Constants.AttributeModifierOperation.ADD).setRegistryName("paralysis"));
    }

    @SubscribeEvent
    public static void registerPotionTypes(RegistryEvent.Register<PotionType> event)
    {

    }

    @SubscribeEvent
    public static void onPlayerAttack(AttackEntityEvent event)
    {
        EntityPlayer player = event.getEntityPlayer();
        if (!player.world.isRemote && player.isPotionActive(PARALYSIS))
        {
            if (player.world.rand.nextBoolean())
            {
                double xOffset = -MathHelper.sin(player.rotationYaw * 0.017453292F);
                double zOffset = MathHelper.cos(player.rotationYaw * 0.017453292F);
                player.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, player.posX + xOffset, player.posY + player.height * 0.5D, player.posZ + zOffset, xOffset, 0.0D, zOffset);
                event.setCanceled(true);
            }
        }
    }
}
