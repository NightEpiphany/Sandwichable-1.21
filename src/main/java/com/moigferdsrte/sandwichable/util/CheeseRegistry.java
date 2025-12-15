package com.moigferdsrte.sandwichable.util;

import com.google.common.collect.Maps;
import com.moigferdsrte.sandwichable.Sandwichable;
import com.moigferdsrte.sandwichable.blocks.extra.BasinContent;
import com.moigferdsrte.sandwichable.blocks.extra.BasinContentType;
import com.moigferdsrte.sandwichable.items.extra.CheeseType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.Map;

public class CheeseRegistry {
    public static final CheeseRegistry INSTANCE = new CheeseRegistry();

    private final Map<String, CheeseType> cheeseTypes = Maps.newHashMap();
    private final Map<String, BasinContent> basinContents = Maps.newHashMap();
    private final Map<CheeseType, BasinContent> typeToCheese = Maps.newHashMap();
    private final Map<CheeseType, BasinContent> typeToFermentingMilk = Maps.newHashMap();
    private final Map<CheeseType, Item> typeToCheeseItem = Maps.newHashMap();

    private CheeseRegistry() {
        this.typeToCheeseItem.put(CheeseType.REGULAR, Items.BUCKET);
        this.typeToCheeseItem.put(CheeseType.CREAMY, Items.BUCKET);
        this.typeToCheeseItem.put(CheeseType.INTOXICATING, Items.BUCKET);
        this.typeToCheeseItem.put(CheeseType.SOUR, Items.BUCKET);
        this.typeToCheeseItem.put(CheeseType.CANDESCENT, Items.BUCKET);
        this.typeToCheeseItem.put(CheeseType.WARPED_BLEU, Items.BUCKET);
    }

    public void register(CheeseType type) {
        this.cheeseTypes.put(type.toString(), type);
    }

    public void register(BasinContent type) {
        this.basinContents.put(type.toString(), type);
        if(type.getContentType() == BasinContentType.CHEESE) {
            this.typeToCheese.put(type.getCheeseType(), type);
        } else if(type.getContentType() == BasinContentType.FERMENTING_MILK) {
            this.typeToFermentingMilk.put(type.getCheeseType(), type);
        }
    }

    public BasinContent basinContentFromString(String id) {
        return this.basinContents.get(id);
    }
    public CheeseType cheeseTypeFromString(String id) {
        return this.cheeseTypes.get(id);
    }
    public BasinContent cheeseFromCheeseType(CheeseType type) {
        return this.typeToCheese.get(type);
    }
    public ItemStack cheeseItemFromCheeseType(CheeseType type) {
        if(type == null) {
            Sandwichable.LOGGER.error("NULL CheeseType");
            return ItemStack.EMPTY;
        }
        return new ItemStack(this.typeToCheeseItem.get(type));
    }
    public BasinContent fermentingMilkFromCheeseType(CheeseType type) {
        return this.typeToFermentingMilk.get(type);
    }
}
