package com.moigferdsrte.sandwichable.recipe.special;

import com.google.common.base.Objects;
import com.moigferdsrte.sandwichable.items.BiomeVariantItem;
import com.moigferdsrte.sandwichable.registry.ItemsRegistry;
import com.moigferdsrte.sandwichable.registry.RecipeRegistry;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.component.DataComponentTypes.CUSTOM_DATA;

public class AncientGrainBreadRecipe extends SpecialCraftingRecipe {
    public AncientGrainBreadRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        List<ItemStack> matches = new ArrayList<>();
        for (int y = 0; y < input.getHeight(); y++) {
            if (!matches.isEmpty() && matches.size() < 3) {
                return false;
            }
            for (int x = 0; x < input.getWidth(); x++) {
                var invStack = input.getStackInSlot((y * input.getWidth()) + x);
                if (matches.size() >= 3) {
                    if (!invStack.isEmpty()) {
                        return false;
                    }
                } else {
                    if (invStack.isOf(ItemsRegistry.ANCIENT_GRAIN)) {
                        matches.add(invStack);
                    } else if (invStack.isEmpty()) {
                        if (!matches.isEmpty()) {
                            break;
                        }
                    } else {
                        return false;
                    }
                }
            }
        }

        if (matches.size() == 3) {
            for (var match : matches) {
                if (!Objects.equal(match.getOrDefault(CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt(), matches.get(0).getOrDefault(CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt())) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        var result = new ItemStack(ItemsRegistry.ANCIENT_GRAIN_BREAD);

        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                var stack = input.getStackInSlot((y * input.getWidth()) + x);

                if (stack.isOf(ItemsRegistry.ANCIENT_GRAIN)) {
                    BiomeVariantItem.copyBiome(stack, result);

                    return result;
                }
            }
        }

        return result;
    }

    @Override
    public boolean fits(int width, int height) {
        return width >= 3 && height >= 1;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.ANCIENT_GRAIN_BREAD;
    }
}
