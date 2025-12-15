package com.moigferdsrte.sandwichable.network.payload;

import com.moigferdsrte.sandwichable.util.Util;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record MinecartClientSyncPayload(int id) implements CustomPayload {

    public static final PacketCodec<PacketByteBuf, MinecartClientSyncPayload> CODEC = CustomPayload.codecOf(
            MinecartClientSyncPayload::write,
            MinecartClientSyncPayload::new
    );

    public static final Id<MinecartClientSyncPayload> ID = new Id<>(Util.id("request_sandwich_table_cart_sync"));

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    private MinecartClientSyncPayload(PacketByteBuf buf) {
        this(buf.readInt());
    }

    private void write(PacketByteBuf buf) {
        buf.writeInt(this.id);
    }
}
