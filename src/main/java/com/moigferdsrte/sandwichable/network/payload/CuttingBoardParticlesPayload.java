package com.moigferdsrte.sandwichable.network.payload;

import com.moigferdsrte.sandwichable.util.Util;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

public record CuttingBoardParticlesPayload(ItemStack stack, int depth, BlockPos pos) implements CustomPayload {

    public static final PacketCodec<RegistryByteBuf, CuttingBoardParticlesPayload> CODEC = CustomPayload.codecOf(
            CuttingBoardParticlesPayload::write,
            CuttingBoardParticlesPayload::new
    );

    public static final Id<CuttingBoardParticlesPayload> ID = new Id<>(Util.id("cutting_board_particles"));

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    private CuttingBoardParticlesPayload(RegistryByteBuf buf) {
        this(ItemStack.OPTIONAL_PACKET_CODEC.decode(buf), buf.readInt(), buf.readBlockPos());
    }

    private void write(RegistryByteBuf buf) {
        ItemStack.OPTIONAL_PACKET_CODEC.encode(buf, stack());
        buf.writeInt(depth());
        buf.writeBlockPos(pos());
    }
}
