package com.moigferdsrte.sandwichable.blocks.entity.screen.data;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.BlockPos;

public record BottleCratePos(BlockPos pos) {
    public static final PacketCodec<RegistryByteBuf, BottleCratePos> PACKET_CODEC = PacketCodec.tuple(
            BlockPos.PACKET_CODEC,
            BottleCratePos::pos,
            BottleCratePos::new
    );
}
