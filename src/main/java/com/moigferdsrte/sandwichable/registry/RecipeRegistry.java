package com.moigferdsrte.sandwichable.registry;

import com.moigferdsrte.sandwichable.recipe.CuttingRecipe;
import com.moigferdsrte.sandwichable.recipe.ToastingRecipe;
import com.moigferdsrte.sandwichable.recipe.serializer.AncientGrainBreadSliceRecipeSerializer;
import com.moigferdsrte.sandwichable.recipe.serializer.CuttingRecipeSerializer;
import com.moigferdsrte.sandwichable.recipe.serializer.ToastedAncientGrainBreadSliceRecipeSerializer;
import com.moigferdsrte.sandwichable.recipe.serializer.ToastingRecipeSerializer;
import com.moigferdsrte.sandwichable.recipe.special.AncientGrainBreadRecipe;
import com.moigferdsrte.sandwichable.recipe.special.AncientGrainBreadSliceRecipe;
import com.moigferdsrte.sandwichable.recipe.special.ToastedAncientGrainBreadSliceRecipe;
import com.moigferdsrte.sandwichable.util.Util;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class RecipeRegistry {
    public static RecipeType<CuttingRecipe> CUTTING_RECIPE;
    public static RecipeType<ToastingRecipe> TOASTING_RECIPE;


    public static final SpecialRecipeSerializer<AncientGrainBreadRecipe> ANCIENT_GRAIN_BREAD = Registry.register(
            Registries.RECIPE_SERIALIZER, Util.id("crafting_special_ancientgrainbread"), new SpecialRecipeSerializer<>(AncientGrainBreadRecipe::new));
    public static final AncientGrainBreadSliceRecipeSerializer ANCIENT_GRAIN_BREAD_SLICE = Registry.register(
            Registries.RECIPE_SERIALIZER, Util.id("cutting_special_ancientgrainbreadslice"), AncientGrainBreadSliceRecipeSerializer.INSTANCE);
    public static final ToastedAncientGrainBreadSliceRecipeSerializer TOASTED_ANCIENT_GRAIN_BREAD_SLICE = Registry.register(
            Registries.RECIPE_SERIALIZER, Util.id("toasting_special_toastedancientgrainbreadslice"), ToastedAncientGrainBreadSliceRecipeSerializer.INSTANCE);
    public static void init() {
        CUTTING_RECIPE = Registry.register(Registries.RECIPE_TYPE, Util.id(CuttingRecipe.Type.ID), CuttingRecipe.Type.INSTANCE);
        TOASTING_RECIPE = Registry.register(Registries.RECIPE_TYPE, Util.id(ToastingRecipe.Type.ID), ToastingRecipe.Type.INSTANCE);
        Registry.register(Registries.RECIPE_SERIALIZER, CuttingRecipeSerializer.ID, CuttingRecipeSerializer.INSTANCE);
        Registry.register(Registries.RECIPE_SERIALIZER, ToastingRecipeSerializer.ID, ToastingRecipeSerializer.INSTANCE);
    }
}
