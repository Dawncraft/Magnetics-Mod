package io.github.dawncraft.magnetics.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.GameData;

/**
 * Register some block entities
 *
 * @author QingChenW
 */
public class ModTileEntities
{
    public static void init()
    {
        registerTileEntity(TileEntityMagnetChest.class, "MagnetChest");
        registerTileEntity(TileEntityMagnetDoor.class, "MagnetDoor");
    }

    /**
     * Register a tile entity
     *
     * @param tileEntityClass
     * @param id
     */
    private static void registerTileEntity(Class<? extends TileEntity> tileEntityClass, String id)
    {
        GameRegistry.registerTileEntity(tileEntityClass, GameData.checkPrefix(id, true));
    }
}
