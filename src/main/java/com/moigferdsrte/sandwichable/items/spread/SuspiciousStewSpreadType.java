package com.moigferdsrte.sandwichable.items.spread;

import net.minecraft.component.type.SuspiciousStewEffectsComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SuspiciousStewItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.world.World;

import static net.minecraft.component.DataComponentTypes.SUSPICIOUS_STEW_EFFECTS;

public class SuspiciousStewSpreadType extends SpreadType {
    public SuspiciousStewSpreadType() {
        super(6, 0.6F, 0xC3C45E, Items.SUSPICIOUS_STEW, Items.BOWL);
    }

    @Override
    public void finishUsing(ItemStack stack, World world, LivingEntity user) {
        SuspiciousStewEffectsComponent tag = stack.get(SUSPICIOUS_STEW_EFFECTS);
        if (tag != null) {
            var effects = tag.effects();
            for (SuspiciousStewEffectsComponent.StewEffect effect : effects) {
                int duration = 160;
                if (effect != null) {
                    duration = effect.duration();
                }
                if (effect == null) {
                    continue;
                }
                var statusEffect = effect.effect();
                if (statusEffect != null) {
                    user.addStatusEffect(new StatusEffectInstance(statusEffect, duration));
                }
            }
        }
    }

    @Override
    public void onPour(ItemStack container, ItemStack spread, NbtCompound nbt) {
        if (container.contains(SUSPICIOUS_STEW_EFFECTS)) {
            var sus = container.get(SUSPICIOUS_STEW_EFFECTS);
            NbtCompound contentsNbt = new NbtCompound();
            for (var effect : sus.effects()) {
                contentsNbt.putString("effect", effect.effect().getIdAsString());
                contentsNbt.putInt("duration", effect.duration());
            }
            nbt.put("stewEffects", contentsNbt);
        }
    }
}
