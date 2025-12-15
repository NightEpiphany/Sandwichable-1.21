package com.moigferdsrte.sandwichable.items;

import com.moigferdsrte.sandwichable.util.Util;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;

public class InfoTooltipItem extends Item {
    public InfoTooltipItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        Util.appendInfoTooltip(tooltip, this.getTranslationKey());
    }
}
