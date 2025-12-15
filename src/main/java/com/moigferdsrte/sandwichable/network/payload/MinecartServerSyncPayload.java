package com.moigferdsrte.sandwichable.network.payload;

import com.moigferdsrte.sandwichable.util.Util;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record MinecartServerSyncPayload(int specId, NbtCompound nbt) implements CustomPayload {

    public static final Id<MinecartServerSyncPayload> ID = new Id<>(Util.id("sync_sandwich_table_cart"));

    public static final PacketCodec<RegistryByteBuf, MinecartServerSyncPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT, MinecartServerSyncPayload::specId,
            PacketCodecs.NBT_COMPOUND, MinecartServerSyncPayload::nbt,
            MinecartServerSyncPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
