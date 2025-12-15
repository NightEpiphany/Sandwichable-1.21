package com.moigferdsrte.sandwichable.entity;

import com.moigferdsrte.sandwichable.network.payload.MinecartClientSyncPayload;
import com.moigferdsrte.sandwichable.network.payload.MinecartServerSyncPayload;
import com.moigferdsrte.sandwichable.registry.BlocksRegistry;
import com.moigferdsrte.sandwichable.registry.EntitiesRegistry;
import com.moigferdsrte.sandwichable.registry.ItemsRegistry;
import com.moigferdsrte.sandwichable.util.Sandwich;
import com.moigferdsrte.sandwichable.util.SandwichHolder;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class SandwichTableMinecartEntity extends AbstractMinecartEntity implements SandwichHolder {
    private final Sandwich sandwich = new Sandwich();

    public static final MinecraftClient mc = MinecraftClient.getInstance();

    public SandwichTableMinecartEntity(EntityType<SandwichTableMinecartEntity> type, World world) {
        super(type, world);
    }

    public SandwichTableMinecartEntity(World world) {
        super(EntitiesRegistry.SANDWICH_TABLE_MINECART, world);
    }

    public SandwichTableMinecartEntity(World world, double x, double y, double z) {
        super(EntitiesRegistry.SANDWICH_TABLE_MINECART, world, x, y, z);
    }


    @Override
    public Type getMinecartType() {
        return Type.RIDEABLE;
    }

    @Override
    public void onActivatorRail(int x, int y, int z, boolean powered) {
        super.onActivatorRail(x, y, z, powered);
        if(powered) {
            sandwich.ejectSandwich(getWorld(), getPos());
            sync();
        }
    }

    public void sync() {
        if(!getWorld().isClient) {
            NbtCompound t = new NbtCompound();
            writeSandwichTableData(t);
            for(PlayerEntity player : getWorld().getPlayers()) {
                if (player instanceof ServerPlayerEntity) ServerPlayNetworking.send((ServerPlayerEntity)player, new MinecartServerSyncPayload(getId(), t));
            }
        }
    }

    public void clientSync() {
        if(getWorld().isClient) {
            ClientPlayNetworking.send(new MinecartClientSyncPayload(getId()));
        }
    }

    public void readSandwichTableData(NbtCompound tag) {
        sandwich.setFromNbt(tag, mc.world.getRegistryManager());
    }

    public void writeSandwichTableData(NbtCompound tag) {
        sandwich.writeToNbt(tag);
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        if (!getWorld().isClient()) {
            sandwich.interact(getWorld(), getPos(), player, hand, player.isSneaking(), player.getWorld().getRegistryManager());
            sync();
        }
        return ActionResult.success(getWorld().isClient());
    }

    @Override
    public ItemStack getPickBlockStack() {
        return new ItemStack(ItemsRegistry.SANDWICH_TABLE_MINECART);
    }

    @Override
    public void setId(int id) {
        super.setId(id);
        clientSync();
    }

    @Override
    protected void killAndDropSelf(DamageSource source) {
        super.killAndDropSelf(source);
        this.sandwich.ejectSandwich(getWorld(), getPos());
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound tag) {
        super.readCustomDataFromNbt(tag);
        readSandwichTableData(tag);
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound tag) {
        super.writeCustomDataToNbt(tag);
        writeSandwichTableData(tag);
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket(EntityTrackerEntry entityTrackerEntry) {
        return super.createSpawnPacket(entityTrackerEntry);
    }

    @Override
    public BlockState getContainedBlock() {
        return BlocksRegistry.SANDWICH_TABLE.getDefaultState();
    }

    @Override
    public Sandwich getSandwich() {
        return sandwich;
    }

    @Override
    public Item asItem() {
        return ItemsRegistry.SANDWICH_TABLE_MINECART;
    }
}
