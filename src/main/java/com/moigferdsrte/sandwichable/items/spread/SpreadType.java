package com.moigferdsrte.sandwichable.items.spread;

import com.google.common.collect.ImmutableList;
import com.moigferdsrte.sandwichable.registry.ItemsRegistry;
import com.moigferdsrte.sandwichable.util.SpreadRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;

import java.util.List;

public class SpreadType {

    private final int hunger;
    private final float saturation;
    private final int color;
    List<StatusEffectInstance> effects;
    ItemConvertible container;
    ItemConvertible resultContainer;

    public SpreadType(int hunger, float saturationModifier, int color, List<StatusEffectInstance> effects, ItemConvertible container, ItemConvertible resultContainer) {
        this.hunger = hunger;
        this.saturation = saturationModifier;
        this.color = color;
        this.effects = effects;
        this.container = container;
        this.resultContainer = resultContainer;
    }
    public SpreadType(int hunger, float saturationModifier, int color, ItemConvertible container, ItemConvertible resultContainer) {
        this(hunger, saturationModifier, color, ImmutableList.of(), container, resultContainer);
    }

    public int getColor(ItemStack stack) { return color; }

    public int getColor() {
        return color;
    }

    public int getHunger() { return hunger; }

    public float getSaturationModifier() { return saturation; }

    public List<StatusEffectInstance> getStatusEffects(ItemStack stack) { return effects; }

    public ItemConvertible getContainingItem() { return container; }

    public ItemStack getResultItem() { return new ItemStack(resultContainer); }

    public void finishUsing(ItemStack stack, World world, LivingEntity user) {}

    public void onPour(ItemStack container, ItemStack spread) {}

    public String getTranslationKey(String id, ItemStack stack) { return "item.sandwichable.spread."+id; }

    public boolean hasGlint(ItemStack stack) { return false; }
}
