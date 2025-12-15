package com.moigferdsrte.sandwichable.compact.rei;

import com.google.common.collect.ImmutableList;
import com.moigferdsrte.sandwichable.recipe.ToastingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeEntry;

import java.util.Collections;
import java.util.List;

public class ToastingDisplay implements Display {
    private final List<EntryIngredient> inputs;
    private final List<EntryIngredient> results;

    public ToastingDisplay(Ingredient input, ItemStack result) {
        ImmutableList.Builder<EntryIngredient> b = new ImmutableList.Builder<>();
        for(ItemStack i : input.getMatchingStacks()) b.add(EntryIngredient.of(EntryStack.of(VanillaEntryTypes.ITEM, i)));
        this.inputs = b.build();
        this.results = Collections.singletonList(EntryIngredient.of(EntryStack.of(VanillaEntryTypes.ITEM, result)));
    }

    public ToastingDisplay(RecipeEntry<ToastingRecipe> toastingRecipeRecipeEntry) {
        this(toastingRecipeRecipeEntry.value().getInput(), toastingRecipeRecipeEntry.value().getOutputStack());
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return this.inputs;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return this.results;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return ToastingCategory.ID;
    }
}
