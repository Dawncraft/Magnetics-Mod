package io.github.dawncraft.magnetics.util;

import java.util.List;

import io.github.dawncraft.magnetics.potion.ModPotions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

/**
 * Utils
 *
 * @author QingChenW
 */
public class Utils
{
    private Utils() {}

    /**
     * 有点问题,等待重写
     *
     * @param entity
     * @param reachDistance
     * @return
     */
    public static RayTraceResult rayTrace(Entity entity, double reachDistance)
    {
        Vec3d start = entity.getPositionEyes(1.0F);
        Vec3d vec = entity.getLook(1.0F).scale(reachDistance + 1);
        Vec3d end = start.add(vec);
        RayTraceResult raytraceResult = entity.world.rayTraceBlocks(start, end);

        Entity target = null;
        double distance = 0.0D;
        List<Entity> list = entity.world.getEntitiesWithinAABBExcludingEntity(entity, entity.getEntityBoundingBox().expand(vec.x, vec.y, vec.z));
        for (Entity query : list)
        {
            if (query.canBeCollidedWith())
            {
                AxisAlignedBB axisalignedbb = query.getEntityBoundingBox().grow(0.3D);
                RayTraceResult raytraceResult2 = axisalignedbb.calculateIntercept(start, end);
                if (raytraceResult2 != null)
                {
                    double query_distance = start.squareDistanceTo(raytraceResult2.hitVec);
                    if (query_distance < distance || distance == 0.0D)
                    {
                        target = query;
                        distance = query_distance;
                    }
                }
            }
        }

        if (target != null)
        {
            raytraceResult = new RayTraceResult(target);
        }

        return raytraceResult;
    }

    /**
     * 不重新实现会导致闪电劈到自己,我可不想玩三国杀
     *
     * @param owner 闪电的发出者
     * @param world 世界
     * @param x 要劈的x坐标
     * @param y 要劈的y坐标
     * @param z 要劈的z坐标
     */
    public static void summonLightningBoltAt(Entity owner, World world, double x, double y, double z)
    {
        EntityLightningBolt entityLightningBolt = new EntityLightningBolt(world, x, y, z, true);
        world.addWeatherEffect(entityLightningBolt);
        for (Entity entity : world.getEntitiesWithinAABBExcludingEntity(entityLightningBolt, new AxisAlignedBB(x, y, z, x, y + 6.0D, z).grow(3.0D)))
        {
            if (entity != owner && !ForgeEventFactory.onEntityStruckByLightning(entity, entityLightningBolt))
            {
                entity.onStruckByLightning(entityLightningBolt);
                if (entity instanceof EntityLivingBase) ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(ModPotions.PARALYSIS, 60));
            }
        }
    }
}
