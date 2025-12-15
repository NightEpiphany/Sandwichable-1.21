package com.moigferdsrte.sandwichable.recipe;

import com.moigferdsrte.sandwichable.recipe.api.IGenericRecipe;
import com.moigferdsrte.sandwichable.recipe.serializer.CuttingRecipeSerializer;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class CuttingRecipe implements Recipe<SingleStackRecipeInput> {
    private final Ingredient input;
    private final ItemStack output;
    private final Identifier id;

    public CuttingRecipe(Ingredient input, ItemStack output, Identifier id) {
        this.input = input;
        this.output = output;
        this.id = id;
    }

    public ItemStack getOutputStack() {
        return output;
    }

    public Ingredient getInput() {
        return input;
    }

    public Identifier getId() {
        return id;
    }

    @Override
    public boolean matches(SingleStackRecipeInput inputs, World world) {
        return input.test(inputs.getStackInSlot(0));
    }

    @Override
    public ItemStack craft(SingleStackRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return getOutputStack();
    }

    @Override
    public boolean fits(int width, int height) {
        return false;
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return true;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CuttingRecipeSerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<CuttingRecipe> {
        private Type() {}
        public static final CuttingRecipe.Type INSTANCE = new CuttingRecipe.Type();

        public static final String ID = "toasting_recipe";
    }

    public static abstract class Special extends CuttingRecipe implements IGenericRecipe {

        public Special(Identifier id) {
            super(Ingredient.EMPTY, ItemStack.EMPTY, id);
        }

        @Override
        public abstract ItemStack getOutputStack();

        @Override
        public abstract ItemStack craft(SingleStackRecipeInput input, RegistryWrapper.WrapperLookup lookup);

        @Override
        public abstract boolean matches(SingleStackRecipeInput inputs, World world);

        @Override
        public abstract boolean fits(int width, int height);

        @Override
        public abstract RecipeSerializer<?> getSerializer();
    }
}
