package com.moigferdsrte.sandwichable.recipe.special;

import com.moigferdsrte.sandwichable.items.BiomeVariantItem;
import com.moigferdsrte.sandwichable.recipe.ToastingRecipe;
import com.moigferdsrte.sandwichable.registry.ItemsRegistry;
import com.moigferdsrte.sandwichable.registry.RecipeRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ToastedAncientGrainBreadSliceRecipe extends ToastingRecipe.Special {
    public ToastedAncientGrainBreadSliceRecipe(Identifier id) {
        super(id);
    }

    @Override
    public ItemStack craft(SingleStackRecipeInput inv, RegistryWrapper.WrapperLookup registryManager) {
        var stack = getResult(registryManager);

        // The inventory is always a single slot inventory containing the ingredient, not the entire toaster
        BiomeVariantItem.copyBiome(inv.getStackInSlot(0), stack);

        return stack;
    }

    @Override
    public boolean matches(SingleStackRecipeInput inv, World world) {
        return inv.getStackInSlot(0).isOf(ItemsRegistry.ANCIENT_GRAIN_BREAD_SLICE);
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.TOASTED_ANCIENT_GRAIN_BREAD_SLICE;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return new ItemStack(ItemsRegistry.TOASTED_ANCIENT_GRAIN_BREAD_SLICE, 1);
    }
}
