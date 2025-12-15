package com.moigferdsrte.sandwichable.blocks.extra;

import com.moigferdsrte.sandwichable.fluids.FluidsRegistry;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.StringIdentifiable;

public enum FluidType implements StringIdentifiable {
    NONE("none", Fluids.EMPTY.getDefaultState()), WATER("water", Fluids.WATER.getDefaultState()), PICKLE_BRINE("brine", FluidsRegistry.PICKLE_BRINE.getDefaultState());
    private final String name;
    public final FluidState state;

    FluidType(String name, FluidState state) {
        this.name = name;
        this.state = state;
    }

    @Override
    public String asString() {
        return name;
    }
}
