package com.moigferdsrte.sandwichable.compact.appleskin;

import com.moigferdsrte.sandwichable.items.SandwichBlockItem;
import com.moigferdsrte.sandwichable.items.extra.DynamicFood;
import com.moigferdsrte.sandwichable.util.Sandwich;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.type.FoodComponent;
import squeek.appleskin.api.AppleSkinApi;
import squeek.appleskin.api.event.FoodValuesEvent;

import static net.minecraft.component.DataComponentTypes.FOOD;

public class SandwichableAppleSkin implements AppleSkinApi {
    @Override
    public void registerEvents() {
        FoodValuesEvent.EVENT.register(event -> {
            var stack = event.itemStack;
            if (stack.getItem() instanceof SandwichBlockItem sandwich) {
                Sandwich.DisplayValues vals = sandwich.getDisplayValues(event.itemStack, event.player.getWorld().getRegistryManager());
                var defaultVals = stack.get(FOOD);
                event.modifiedFoodComponent = new FoodComponent(vals.hunger(), vals.saturation(), true, defaultVals.eatSeconds(), defaultVals.usingConvertsTo(), defaultVals.effects());
            } else if (stack.getItem() instanceof DynamicFood food) {
                var defaultVals = stack.get(FOOD);
                event.modifiedFoodComponent = new FoodComponent(food.getRestoredFood(MinecraftClient.getInstance().world, stack), food.getRestoredSaturation(MinecraftClient.getInstance().world, stack),  true, defaultVals.eatSeconds(), defaultVals.usingConvertsTo(), defaultVals.effects());
            }
        });
    }
}
