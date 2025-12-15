package com.moigferdsrte.sandwichable.blocks.entity;

import com.moigferdsrte.sandwichable.blocks.extra.SyncedBlockEntity;
import com.moigferdsrte.sandwichable.registry.BlocksRegistry;
import com.moigferdsrte.sandwichable.util.Sandwich;
import com.moigferdsrte.sandwichable.util.SandwichHolder;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class SandwichTableBlockEntity extends BlockEntity implements SandwichHolder, SyncedBlockEntity {

    private final Sandwich sandwich = new Sandwich();

    public SandwichTableBlockEntity(BlockPos pos, BlockState state) {
        super(BlocksRegistry.SANDWICHTABLE_BLOCKENTITY, pos, state);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        sandwich.setFromNbt(nbt, registryLookup);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        sandwich.writeToNbt(nbt);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        NbtCompound nbt = new NbtCompound();
        this.writeNbt(nbt, registryLookup);
        return nbt;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return this.getPacket();
    }

    @Override
    public Sandwich getSandwich() {
        return sandwich;
    }
}
