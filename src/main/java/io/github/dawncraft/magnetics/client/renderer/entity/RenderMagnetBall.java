package io.github.dawncraft.magnetics.client.renderer.entity;

import io.github.dawncraft.magnetics.entity.projectile.EntityMagnetBall;
import io.github.dawncraft.magnetics.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;

/**
 * Magnet ball
 * 
 * @author QingChenW
 */
public class RenderMagnetBall extends RenderSnowball<EntityMagnetBall>
{
    public RenderMagnetBall(RenderManager renderManager)
    {
        super(renderManager, ModItems.MAGNET_BALL, Minecraft.getMinecraft().getRenderItem());
    }

}
