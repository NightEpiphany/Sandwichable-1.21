package com.moigferdsrte.sandwichable.items.extra;

import com.moigferdsrte.sandwichable.registry.BlocksRegistry;
import com.moigferdsrte.sandwichable.registry.ItemsRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;

import static net.minecraft.component.DataComponentTypes.BLOCK_ENTITY_DATA;

public class SandwichableGroupIconBuilder {
    public static ItemStack getIcon() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return new ItemStack(Items.BREAD);
        DefaultedList<ItemStack> sandwichList = DefaultedList.ofSize(128, ItemStack.EMPTY);
        ItemStack groupIcon = new ItemStack(BlocksRegistry.SANDWICH.asItem());
        sandwichList.set(0, new ItemStack(ItemsRegistry.CHEESE_SLICE_REGULAR));
        sandwichList.set(1, new ItemStack(ItemsRegistry.BACON_STRIPS));
        sandwichList.set(2, new ItemStack(ItemsRegistry.LETTUCE_LEAF));
        sandwichList.set(3, new ItemStack(ItemsRegistry.TOMATO_SLICE));
        sandwichList.set(4, new ItemStack(ItemsRegistry.TOASTED_BREAD_SLICE));
        groupIcon.set(BLOCK_ENTITY_DATA, NbtComponent.of(Inventories.writeNbt(new NbtCompound(), sandwichList, client.world.getRegistryManager())));
        return groupIcon;
    }
}
