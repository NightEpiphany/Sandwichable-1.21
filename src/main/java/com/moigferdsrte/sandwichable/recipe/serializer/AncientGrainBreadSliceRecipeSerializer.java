package com.moigferdsrte.sandwichable.recipe.serializer;

import com.moigferdsrte.sandwichable.recipe.CuttingRecipe;
import com.moigferdsrte.sandwichable.recipe.special.AncientGrainBreadSliceRecipe;
import com.moigferdsrte.sandwichable.util.Util;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;

public class AncientGrainBreadSliceRecipeSerializer implements RecipeSerializer<AncientGrainBreadSliceRecipe> {

    public static final AncientGrainBreadSliceRecipeSerializer INSTANCE = new AncientGrainBreadSliceRecipeSerializer();

    public static final MapCodec<AncientGrainBreadSliceRecipe> CODEC = RecordCodecBuilder.mapCodec(ins -> ins.group(
            Identifier.CODEC.optionalFieldOf("id", Util.id("empty")).forGetter(CuttingRecipe::getId)
    ).apply(ins, AncientGrainBreadSliceRecipe::new));

    public static final PacketCodec<RegistryByteBuf, AncientGrainBreadSliceRecipe> PACKET_CODEC = PacketCodec.tuple(
            Identifier.PACKET_CODEC, CuttingRecipe::getId,
            AncientGrainBreadSliceRecipe::new
    );
    @Override
    public MapCodec<AncientGrainBreadSliceRecipe> codec() {
        return CODEC;
    }

    @Override
    public PacketCodec<RegistryByteBuf, AncientGrainBreadSliceRecipe> packetCodec() {
        return PACKET_CODEC;
    }
}
