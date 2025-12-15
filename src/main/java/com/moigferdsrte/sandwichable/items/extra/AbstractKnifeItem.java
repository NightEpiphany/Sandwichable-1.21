package com.moigferdsrte.sandwichable.items.extra;

import com.moigferdsrte.sandwichable.util.Util;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;

public abstract class AbstractKnifeItem extends SwordItem {
    public AbstractKnifeItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        Util.appendInfoTooltip(tooltip, this.getTranslationKey());
    }
}
