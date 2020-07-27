package io.github.dawncraft.magnetics.client.renderer.entity;

import io.github.dawncraft.magnetics.entity.projectile.EntityMagnetBall;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

/**
 * Register entity render.
 *
 * @author QingChenW
 */
public class ModEntityRenderers
{
    public static void init()
    {
        registerEntityRenderer(EntityMagnetBall.class, RenderMagnetBall.class);
    }

    private static <T extends Entity> void registerEntityRenderer(Class<T> entityClass, Class<? extends Render<T>> render)
    {
        registerEntityRenderer(entityClass, new Factory<T>(render));
    }

    private static <T extends Entity> void registerEntityRenderer(Class<T> entityClass, IRenderFactory<? super T> renderFactory)
    {
        RenderingRegistry.registerEntityRenderingHandler(entityClass, renderFactory);
    }

    /**
     * A default factory of entity renderer.
     *
     * @author ustc-zzzz
     */
    public static class Factory<T extends Entity> implements IRenderFactory<T>
    {
        private final Class<? extends Render<T>> renderClass;

        public Factory(Class<? extends Render<T>> renderClass)
        {
            this.renderClass = renderClass;
        }

        @Override
        public Render<T> createRenderFor(RenderManager manager)
        {
            try
            {
                return this.renderClass.getConstructor(RenderManager.class).newInstance(manager);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}
