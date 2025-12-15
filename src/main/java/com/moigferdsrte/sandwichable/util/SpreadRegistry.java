package com.moigferdsrte.sandwichable.util;

import com.google.common.collect.Maps;
import com.moigferdsrte.sandwichable.Sandwichable;
import com.moigferdsrte.sandwichable.items.SpreadItem;
import com.moigferdsrte.sandwichable.items.spread.*;
import com.moigferdsrte.sandwichable.registry.ItemsRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.HashMap;
import java.util.Map;

public class SpreadRegistry {
    public static final SpreadRegistry INSTANCE = new SpreadRegistry();

    private final Map<String, SpreadType> idToSpreadType = Maps.newHashMap();
    private final Map<SpreadType, String> spreadTypeToString = Maps.newHashMap();
    private final Map<ItemConvertible, SpreadType> spreadContainerToType = new HashMap<>();

    private SpreadRegistry() {}

    public static final SpreadType MUSHROOM_STEW = new SpreadType(6, 0.6F, 0xAD7451, Items.MUSHROOM_STEW, Items.BOWL);
    public static final SpreadType RABBIT_STEW = new SpreadType(10, 0.6F, 0xBF7234, Items.RABBIT_STEW, Items.BOWL);
    public static final SpreadType BEETROOT_SOUP = new SpreadType(6, 0.6F, 0x8C0023, Items.BEETROOT_SOUP, Items.BOWL);
    public static final SpreadType HONEY = new HoneySpreadType();
    public static final SpreadType SUSPICIOUS_STEW = new SuspiciousStewSpreadType();
    public static final SpreadType FERMENTING_MILK = new FermentingMilkSpreadType();
    public static final SpreadType POTION = new PotionSpreadType();
    public static final SpreadType SWEET_BERRY_JAM = new SpreadType(5, 0.5F, 0xF00024,ItemsRegistry.SWEET_BERRY_JAM, Items.GLASS_BOTTLE);
    public static final SpreadType GLOW_BERRY_JAM = new SpreadType(5, 0.5F, 0xFFCB54, ItemsRegistry.GLOW_BERRY_JAM, Items.GLASS_BOTTLE);
    public static final SpreadType MAYONNAISE = new SpreadType(4, 0.6F, 0xFFD5B5, ItemsRegistry.MAYONNAISE, Items.GLASS_BOTTLE);

    public static void init() {
        SpreadRegistry.INSTANCE.register("mushroom_stew", MUSHROOM_STEW);
        SpreadRegistry.INSTANCE.register("rabbit_stew", RABBIT_STEW);
        SpreadRegistry.INSTANCE.register("beetroot_soup", BEETROOT_SOUP);
        SpreadRegistry.INSTANCE.register("honey", HONEY);
        SpreadRegistry.INSTANCE.register("suspicious_stew", SUSPICIOUS_STEW);
        SpreadRegistry.INSTANCE.register("fermenting_milk", FERMENTING_MILK);
        SpreadRegistry.INSTANCE.register("sweet_berry_jam", SWEET_BERRY_JAM);
        SpreadRegistry.INSTANCE.register("glow_berry_jam", GLOW_BERRY_JAM);
        SpreadRegistry.INSTANCE.register("mayonnaise", MAYONNAISE);
        SpreadRegistry.INSTANCE.register("potion", POTION);
    }

    public SpreadType register(String id, SpreadType type) {
        idToSpreadType.put(id, type);
        spreadTypeToString.put(type, id);
        spreadContainerToType.put(type.getContainingItem(), type);
        return type;
    }

    public SpreadType getSpreadFromItem(ItemConvertible item) {
        return spreadContainerToType.get(item);
    }

    public boolean itemHasSpread(ItemConvertible item) {
        return spreadContainerToType.containsKey(item);
    }

    public Item getItem(SpreadType type) {
        if (type == SpreadRegistry.BEETROOT_SOUP)
            return ItemsRegistry.BEETROOT_SOUP_SPREAD;
        if (type == SpreadRegistry.RABBIT_STEW)
            return ItemsRegistry.RABBIT_STEW_SPREAD;
        if (type == SpreadRegistry.MUSHROOM_STEW)
            return ItemsRegistry.MUSHROOM_STEW_SPREAD;
        if (type == SpreadRegistry.HONEY)
            return ItemsRegistry.HONEY_SPREAD;
        if (type == SpreadRegistry.SUSPICIOUS_STEW)
            return ItemsRegistry.SUSPICIOUS_STEW_SPREAD;
        if (type == SpreadRegistry.FERMENTING_MILK)
            return ItemsRegistry.FERMENTING_MILK_SPREAD;
        if (type == SpreadRegistry.POTION)
            return ItemsRegistry.POTION_SPREAD;
        if (type == SpreadRegistry.SWEET_BERRY_JAM)
            return ItemsRegistry.SWEET_BERRY_JAM_SPREAD;
        if (type == SpreadRegistry.GLOW_BERRY_JAM)
            return ItemsRegistry.GLOW_BERRY_JAM_SPREAD;
        if (type == SpreadRegistry.MAYONNAISE)
            return ItemsRegistry.MAYONNAISE_SPREAD;
        return ItemStack.EMPTY.getItem();
    }

    public SpreadType fromString(String id) {
        if (id == null) {
            Sandwichable.LOGGER.warn("NULL SpreadType ID");
            return null;
        }
        return idToSpreadType.get(id);
    }
    public String asString(SpreadType type) {
        return spreadTypeToString.get(type);
    }
}
