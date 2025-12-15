package com.moigferdsrte.sandwichable.items.extra;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface DynamicFood {
    int getRestoredFood(World world, ItemStack stack);

    float getRestoredSaturation(World world, ItemStack stack);
}
