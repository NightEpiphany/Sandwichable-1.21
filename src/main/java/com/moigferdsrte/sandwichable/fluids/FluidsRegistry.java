package com.moigferdsrte.sandwichable.fluids;

import com.moigferdsrte.sandwichable.util.Util;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class FluidsRegistry {

    public static final FlowableFluid PICKLE_BRINE = Registry.register(Registries.FLUID, Util.id("pickle_brine"), new PickleBrineFluid.Still());
    public static final FlowableFluid PICKLE_BRINE_FLOWING = Registry.register(Registries.FLUID, Util.id("pickle_brine_flowing"), new PickleBrineFluid.Flowing());

    public static void init() {
    }
}
