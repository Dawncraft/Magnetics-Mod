package io.github.dawncraft.magnetics.world.gen.feature;

import java.util.Random;

import io.github.dawncraft.magnetics.block.ModBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Register some ore generators.
 *
 * @author QingChenW
 */
public class ModWorldGenerators
{
    private static BlockPos lastOrePos;

    public static WorldGenerator magnetOreGenerator = new WorldGenMinable(ModBlocks.MAGNET_ORE.getDefaultState(), 6)
    {
        @Override
        public boolean generate(World world, Random rand, BlockPos position)
        {
            if (TerrainGen.generateOre(world, rand, this, position, OreGenEvent.GenerateMinable.EventType.CUSTOM))
            {
                if (world.provider.getDimensionType() == DimensionType.OVERWORLD)
                {
                    for (int i = 0; i < 8; ++i)
                    {
                        BlockPos blockpos = new BlockPos(position.getX() + rand.nextInt(16), 1 + rand.nextInt(62), position.getZ() + rand.nextInt(16));
                        super.generate(world, rand, blockpos);
                    }
                }
            }
            return true;
        }
    };

    public static void init()
    {
        MinecraftForge.TERRAIN_GEN_BUS.register(ModWorldGenerators.class);
        MinecraftForge.ORE_GEN_BUS.register(ModWorldGenerators.class);
    }

    @SubscribeEvent
    public static void onOreGenPost(OreGenEvent.Post event)
    {
        if (!event.getPos().equals(lastOrePos))
        {
            lastOrePos = event.getPos();
            magnetOreGenerator.generate(event.getWorld(), event.getRand(), event.getPos());
        }
    }

    @SubscribeEvent
    public static void onOreGenGenerateMinable(OreGenEvent.GenerateMinable event)
    {

    }

    private static void registerGenerator(IWorldGenerator generator, int weight)
    {
        GameRegistry.registerWorldGenerator(generator, weight);
    }
}
