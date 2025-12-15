package com.moigferdsrte.sandwichable.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class PickleBrineFluidBlock extends FluidBlock {
    public PickleBrineFluidBlock(FlowableFluid fluid, Settings settings) {
        super(fluid, settings);
    }

    @Override public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) { return true; }
}
