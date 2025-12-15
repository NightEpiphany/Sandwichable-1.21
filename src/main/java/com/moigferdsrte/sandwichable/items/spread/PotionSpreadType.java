package com.moigferdsrte.sandwichable.items.spread;

import com.google.common.collect.Lists;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.potion.Potion;

import java.util.List;

import static net.minecraft.component.DataComponentTypes.CUSTOM_DATA;
import static net.minecraft.component.DataComponentTypes.POTION_CONTENTS;

public class PotionSpreadType extends SpreadType {
    public PotionSpreadType() {
        super(0, 0.0f, 0, Items.POTION, Items.GLASS_BOTTLE);
    }

    @Override
    public int getColor(ItemStack stack) {
        if (!stack.contains(CUSTOM_DATA)) return 0x8C0023;
        var nbt = stack.get(CUSTOM_DATA).copyNbt();
        if (nbt.contains("potionEffects", NbtElement.COMPOUND_TYPE)) {
            return nbt.getInt("color");
        }
        return 0x8C0023;
    }

    @Override
    public List<StatusEffectInstance> getStatusEffects(ItemStack stack) {
        List<StatusEffectInstance> list = Lists.newArrayList();
        if (!stack.contains(POTION_CONTENTS)) return list;
        stack.get(POTION_CONTENTS).getEffects().forEach(list::add);
        return list;
    }

    @Override
    public void onPour(ItemStack container, ItemStack spread, NbtCompound nbt) {
        if (container.contains(POTION_CONTENTS)) {
            var contents = container.get(POTION_CONTENTS);
            NbtCompound contentsNbt = new NbtCompound();
            contents.forEachEffect(effect -> {
                if (!contentsNbt.contains("effect")) contentsNbt.putString("effect", effect.getEffectType().getIdAsString());
                else contentsNbt.putString("effect2", effect.getEffectType().getIdAsString());
                if (!contentsNbt.contains("duration")) contentsNbt.putInt("duration", effect.getDuration());
                else contentsNbt.putInt("duration2", effect.getDuration());
                if (!contentsNbt.contains("amplifier")) nbt.putInt("amplifier", effect.getAmplifier());
                else nbt.putInt("amplifier2", effect.getAmplifier());
                nbt.putInt("color", effect.getEffectType().value().getColor());
            });
            nbt.put("potionEffects", contentsNbt);
        }
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
