package com.moigferdsrte.sandwichable.recipe.serializer;

import com.moigferdsrte.sandwichable.recipe.ToastingRecipe;
import com.moigferdsrte.sandwichable.recipe.special.ToastedAncientGrainBreadSliceRecipe;
import com.moigferdsrte.sandwichable.util.Util;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;

public class ToastedAncientGrainBreadSliceRecipeSerializer implements RecipeSerializer<ToastedAncientGrainBreadSliceRecipe> {

    public static final ToastedAncientGrainBreadSliceRecipeSerializer INSTANCE = new ToastedAncientGrainBreadSliceRecipeSerializer();

    public static final MapCodec<ToastedAncientGrainBreadSliceRecipe> CODEC = RecordCodecBuilder.mapCodec(ins -> ins.group(
            Identifier.CODEC.optionalFieldOf("id", Util.id("empty")).forGetter(ToastingRecipe::getId)
    ).apply(ins, ToastedAncientGrainBreadSliceRecipe::new));

    public static final PacketCodec<RegistryByteBuf, ToastedAncientGrainBreadSliceRecipe> PACKET_CODEC = PacketCodec.tuple(
            Identifier.PACKET_CODEC, ToastingRecipe::getId,
            ToastedAncientGrainBreadSliceRecipe::new
    );

    @Override
    public MapCodec<ToastedAncientGrainBreadSliceRecipe> codec() {
        return CODEC;
    }

    @Override
    public PacketCodec<RegistryByteBuf, ToastedAncientGrainBreadSliceRecipe> packetCodec() {
        return PACKET_CODEC;
    }
}
