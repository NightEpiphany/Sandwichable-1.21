package com.moigferdsrte.sandwichable.registry;

import com.moigferdsrte.sandwichable.entity.SandwichTableMinecartEntity;
import com.moigferdsrte.sandwichable.network.payload.CuttingBoardParticlesPayload;
import com.moigferdsrte.sandwichable.network.payload.MinecartClientSyncPayload;
import com.moigferdsrte.sandwichable.network.payload.MinecartServerSyncPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class NetworkRegistry {

    public static void registerPayload() {
        PayloadTypeRegistry.playC2S().register(MinecartClientSyncPayload.ID, MinecartClientSyncPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(MinecartServerSyncPayload.ID, MinecartServerSyncPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(CuttingBoardParticlesPayload.ID, CuttingBoardParticlesPayload.CODEC);
    }

    public static void serverInit() {
        ServerPlayNetworking.registerGlobalReceiver(MinecartClientSyncPayload.ID, (payload, ctx) -> {
            Entity e = ctx.player().getEntityWorld().getEntityById(payload.id());
            ctx.server().execute(() -> {
                if (e instanceof SandwichTableMinecartEntity) {
                    ((SandwichTableMinecartEntity)e).sync();
                }
            });
        });
    }
}
