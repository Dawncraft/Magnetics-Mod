package io.github.dawncraft.magnetics.event;

import java.util.List;

import io.github.dawncraft.magnetics.MagneticsMod;
import io.github.dawncraft.magnetics.api.item.ItemArmorSuit;
import io.github.dawncraft.magnetics.item.ModItems;
import io.github.dawncraft.magnetics.tileentity.TileEntityLightningArrester;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Events' listener
 *
 * @author QingChenW
 */
@Mod.EventBusSubscriber(modid = MagneticsMod.MODID)
public class EventListener
{
    /**
     * 目前用于处理避雷针吸引闪电
     *
     * @param event 实体加入世界事件
     */
    @SubscribeEvent
    public static void onEntityCreated(EntityJoinWorldEvent event)
    {
        Entity entity = event.getEntity();
        World world = event.getEntity().getEntityWorld();
        if (entity instanceof EntityLightningBolt)
        {
            EntityLightningBolt entityLightningBolt = (EntityLightningBolt) entity;
            BlockPos pos = entityLightningBolt.getPosition();

            TileEntityLightningArrester tileentityLightningArrester = null;
            double distance = 0.0D;
            for (TileEntity tileentity : world.loadedTileEntityList)
            {
                if (tileentity instanceof TileEntityLightningArrester)
                {
                    double query_distance = tileentity.getPos().distanceSq(pos);
                    if (query_distance < 16384.0D && (query_distance < distance || distance == 0.0D))
                    {
                        distance = query_distance;
                        tileentityLightningArrester = (TileEntityLightningArrester) tileentity;
                    }
                }
            }

            if (tileentityLightningArrester != null)
            {
                BlockPos pos2 = tileentityLightningArrester.getPos();
                entityLightningBolt.setLocationAndAngles(pos2.getX(), pos2.getY(), pos2.getZ(), 0.0F, 0.0F);
                if (!world.isRemote) tileentityLightningArrester.lightningStrike();
            }
        }
    }

    /**
     * 目前用于处理磁铁套装增加拾取范围
     *
     * @param event 实体每刻更新时间
     */
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent event)
    {
        if (event.phase == Phase.END && event.side == Side.SERVER)
        {
            EntityPlayer player = event.player;
            if (ItemArmorSuit.getArmorCount(player, ModItems.MAGNET_ARMOR) == 4)
            {
                if (player.getHealth() > 0.0F && !player.isSpectator())
                {
                    AxisAlignedBB axisalignedbb = player.getEntityBoundingBox().grow(4.0D, 1.0D, 4.0D);
                    List<Entity> list = player.world.getEntitiesWithinAABB(EntityItem.class, axisalignedbb);
                    for (Entity entity : list)
                    {
                        if (!entity.isDead)
                        {
                            entity.onCollideWithPlayer(player);
                        }
                    }
                }
            }
        }
    }
}
