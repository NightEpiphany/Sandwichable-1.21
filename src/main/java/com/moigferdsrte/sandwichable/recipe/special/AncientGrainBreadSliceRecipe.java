package com.moigferdsrte.sandwichable.recipe.special;

import com.moigferdsrte.sandwichable.items.BiomeVariantItem;
import com.moigferdsrte.sandwichable.recipe.CuttingRecipe;
import com.moigferdsrte.sandwichable.registry.ItemsRegistry;
import com.moigferdsrte.sandwichable.registry.RecipeRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class AncientGrainBreadSliceRecipe extends CuttingRecipe.Special {
    public AncientGrainBreadSliceRecipe(Identifier id) {
        super(id);
    }


    @Override
    public ItemStack getOutputStack() {
        return new ItemStack(ItemsRegistry.ANCIENT_GRAIN_BREAD_SLICE, 4);
    }

    @Override
    public ItemStack craft(SingleStackRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        var stack = getOutputStack();
        BiomeVariantItem.copyBiome(input.getStackInSlot(0), stack);

        return stack;
    }

    @Override
    public boolean matches(SingleStackRecipeInput inputs, World world) {
        return inputs.getStackInSlot(0).isOf(ItemsRegistry.ANCIENT_GRAIN_BREAD);
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.ANCIENT_GRAIN_BREAD_SLICE;
    }
}
