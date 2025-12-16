package com.moigferdsrte.sandwichable.items;

import net.minecraft.item.Item;
import net.minecraft.util.Rarity;

public class SandwichBookItem extends Item {
    public SandwichBookItem() {
        super(new Item.Settings().maxCount(1).rarity(Rarity.RARE));
    }
}
