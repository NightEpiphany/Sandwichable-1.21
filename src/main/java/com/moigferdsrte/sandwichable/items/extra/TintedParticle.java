package com.moigferdsrte.sandwichable.items.extra;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface TintedParticle {
    int getParticleColor(World world, ItemStack stack);
}
