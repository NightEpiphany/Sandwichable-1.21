package com.moigferdsrte.sandwichable.recipe.serializer;

import com.moigferdsrte.sandwichable.recipe.CuttingRecipe;
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

public class CuttingRecipeSerializer implements RecipeSerializer<CuttingRecipe> {

    private CuttingRecipeSerializer() {}

    public static final CuttingRecipeSerializer INSTANCE = new CuttingRecipeSerializer();
    public static final Identifier ID = Util.id("cutting_recipe");

    public static final MapCodec<CuttingRecipe> CODEC = RecordCodecBuilder.mapCodec(ins -> ins.group(
            Ingredient.ALLOW_EMPTY_CODEC.fieldOf("input").forGetter(CuttingRecipe::getInput),
            ItemStack.CODEC.fieldOf("output").forGetter(CuttingRecipe::getOutputStack),
            Identifier.CODEC.optionalFieldOf("id", Util.id("empty")).forGetter(CuttingRecipe::getId)
    ).apply(ins, CuttingRecipe::new));

    public static final PacketCodec<RegistryByteBuf, CuttingRecipe> PACKET_CODEC = PacketCodec.tuple(
            Ingredient.PACKET_CODEC, CuttingRecipe::getInput,
            ItemStack.PACKET_CODEC, CuttingRecipe::getOutputStack,
            Identifier.PACKET_CODEC, CuttingRecipe::getId,
            CuttingRecipe::new
    );

    @Override
    public MapCodec<CuttingRecipe> codec() {
        return CODEC;
    }

    @Override
    public PacketCodec<RegistryByteBuf, CuttingRecipe> packetCodec() {
        return PACKET_CODEC;
    }
}
