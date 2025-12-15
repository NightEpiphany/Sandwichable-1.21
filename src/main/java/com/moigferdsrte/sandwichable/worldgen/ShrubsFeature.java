package com.moigferdsrte.sandwichable.worldgen;

import com.moigferdsrte.sandwichable.blocks.ShrubBlock;
import com.moigferdsrte.sandwichable.config.SandwichableConfig;
import com.moigferdsrte.sandwichable.registry.BlocksRegistry;
import com.moigferdsrte.sandwichable.util.Util;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class ShrubsFeature extends Feature<DefaultFeatureConfig> {

    public ShrubsFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> ctx) {
        var world = ctx.getWorld();
        var random = ctx.getRandom();

        SandwichableConfig sconfig = Util.getConfig();

        BlockPos pos = world.getTopPosition(Heightmap.Type.WORLD_SURFACE, ctx.getOrigin());

        BlockState blockState = BlocksRegistry.SHRUB.getDefaultState();
        int i = 0;
        for(int j = 0; j < sconfig.shrubGenOptions.spawnTries; j++) {
            BlockPos blockPos = randPosInRadius(random, pos);
            if (world.isAir(blockPos) && blockPos.getY() < 255 && ShrubBlock.canGenerateOn(blockState, world, blockPos)) {
                world.setBlockState(blockPos, blockState, 2);
                i++;
            }
        }

        return i > 0;
    }

    private BlockPos randPosInRadius(Random random, BlockPos pos) {
        int x = pos.getX() + random.nextInt(5 *2) - 5;
        int y = pos.getY() + random.nextInt(5 *2) - 5;
        int z = pos.getZ() + random.nextInt(5 *2) - 5;
        return new BlockPos(x, y, z);
    }
}
