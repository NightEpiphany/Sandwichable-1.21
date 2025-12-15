package com.moigferdsrte.sandwichable.items.spread;

import com.google.common.collect.Lists;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;

import java.util.List;

import static net.minecraft.component.DataComponentTypes.POTION_CONTENTS;

public class PotionSpreadType extends SpreadType {
    public PotionSpreadType() {
        super(0, 0.0f, 0, Items.POTION, Items.GLASS_BOTTLE);
    }

    @Override
    public int getColor(ItemStack stack) {
        if (!stack.contains(POTION_CONTENTS)) return 0;
        return stack.get(POTION_CONTENTS).getColor();
    }

    @Override
    public List<StatusEffectInstance> getStatusEffects(ItemStack stack) {
        List<StatusEffectInstance> list = Lists.newArrayList();
        if (!stack.contains(POTION_CONTENTS)) return list;
        stack.get(POTION_CONTENTS).getEffects().forEach(list::add);
        return list;
    }

    @Override
    public void onPour(ItemStack container, ItemStack spread) {
        if (!container.contains(POTION_CONTENTS)) return;
        var potion = container.get(POTION_CONTENTS);
        spread.set(POTION_CONTENTS, potion);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
