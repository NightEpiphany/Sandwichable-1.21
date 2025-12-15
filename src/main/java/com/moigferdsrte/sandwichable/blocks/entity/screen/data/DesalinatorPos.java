package com.moigferdsrte.sandwichable.blocks.entity.screen.data;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.BlockPos;

public record DesalinatorPos(BlockPos pos) {
    public static final PacketCodec<RegistryByteBuf, DesalinatorPos> PACKET_CODEC = PacketCodec.tuple(
            BlockPos.PACKET_CODEC,
            DesalinatorPos::pos,
            DesalinatorPos::new
    );
}
