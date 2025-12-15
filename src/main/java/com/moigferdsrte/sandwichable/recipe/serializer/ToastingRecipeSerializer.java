package com.moigferdsrte.sandwichable.recipe.serializer;

import com.moigferdsrte.sandwichable.recipe.ToastingRecipe;
import com.moigferdsrte.sandwichable.util.Util;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;

public class ToastingRecipeSerializer implements RecipeSerializer<ToastingRecipe> {
    private ToastingRecipeSerializer() {}

    public static final ToastingRecipeSerializer INSTANCE = new ToastingRecipeSerializer();
    public static final Identifier ID = Util.id("toasting_recipe");

    public static final MapCodec<ToastingRecipe> CODEC = RecordCodecBuilder.mapCodec(ins -> ins.group(
            Ingredient.ALLOW_EMPTY_CODEC.fieldOf("input").forGetter(ToastingRecipe::getInput),
            ItemStack.CODEC.fieldOf("output").forGetter(ToastingRecipe::getOutputStack),
            Identifier.CODEC.optionalFieldOf("id", Util.id("empty")).forGetter(ToastingRecipe::getId)
    ).apply(ins, ToastingRecipe::new));

    public static final PacketCodec<RegistryByteBuf, ToastingRecipe> PACKET_CODEC = PacketCodec.tuple(
            Ingredient.PACKET_CODEC, ToastingRecipe::getInput,
            ItemStack.PACKET_CODEC, ToastingRecipe::getOutputStack,
            Identifier.PACKET_CODEC, ToastingRecipe::getId,
            ToastingRecipe::new
    );

    @Override
    public MapCodec<ToastingRecipe> codec() {
        return CODEC;
    }

    @Override
    public PacketCodec<RegistryByteBuf, ToastingRecipe> packetCodec() {
        return PACKET_CODEC;
    }
}
