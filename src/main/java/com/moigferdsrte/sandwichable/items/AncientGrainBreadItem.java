package com.moigferdsrte.sandwichable.items;

import com.moigferdsrte.sandwichable.items.extra.DynamicFood;
import com.moigferdsrte.sandwichable.items.extra.TintedParticle;
import com.moigferdsrte.sandwichable.util.AncientGrainType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class AncientGrainBreadItem extends BiomeVariantItem implements TintedParticle, DynamicFood {
    private final float foodValueMultiplier;
    private final boolean isSlice;

    public AncientGrainBreadItem(Item.Settings settings, float foodValueMultiplier, boolean isSlice) {
        super(settings);
        this.foodValueMultiplier = foodValueMultiplier;
        this.isSlice = isSlice;
    }

    @Override
    public int getRestoredFood(World world, ItemStack stack) {
        return (int) (AncientGrainType.get(getBiome(world, stack)).food() * foodValueMultiplier);
    }

    @Override
    public float getRestoredSaturation(World world, ItemStack stack) {
        return AncientGrainType.get(getBiome(world, stack)).saturation() * foodValueMultiplier;
    }

    @Override
    public int getParticleColor(World world, ItemStack stack) {
        var type = AncientGrainType.get(getBiome(world, stack));
        return isSlice ? type.breadColor() : type.color();
    }
}
