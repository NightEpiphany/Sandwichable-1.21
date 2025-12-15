package com.moigferdsrte.sandwichable.items;


import com.moigferdsrte.sandwichable.util.Util;
import net.minecraft.block.Block;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;

public class InfoTooltipAliasedBlockItem extends AliasedBlockItem {

    public InfoTooltipAliasedBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        Util.appendInfoTooltip(tooltip, this.getTranslationKey());
    }
}
