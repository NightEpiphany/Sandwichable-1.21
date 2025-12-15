package com.moigferdsrte.sandwichable.items.spread;

import com.moigferdsrte.sandwichable.registry.ItemsRegistry;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import static net.minecraft.component.DataComponentTypes.CUSTOM_DATA;

public class FermentingMilkSpreadType extends SpreadType {
    public FermentingMilkSpreadType() {
        super(3, 0.9F, 0xC9C69D, ItemsRegistry.FERMENTING_MILK_BUCKET, Items.BUCKET);
    }

    @Override
    public void onPour(ItemStack container, ItemStack spread) {
        if(container.contains(CUSTOM_DATA)) {
            NbtComponent.set(CUSTOM_DATA, spread, nbtCompound -> {
                nbtCompound.putInt("effectDuration", container.get(CUSTOM_DATA).copyNbt().getInt("percentFermented")*4);
            });
        }
    }

    @Override
    public void finishUsing(ItemStack stack, World world, LivingEntity user) {
        if(stack.contains(CUSTOM_DATA)) {
            NbtCompound tag = stack.get(CUSTOM_DATA).copyNbt();
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, tag.getInt("effectDuration"), 5));
        }
    }
}
