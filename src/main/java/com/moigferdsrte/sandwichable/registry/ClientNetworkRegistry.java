package com.moigferdsrte.sandwichable.registry;

import com.moigferdsrte.sandwichable.entity.SandwichTableMinecartEntity;
import com.moigferdsrte.sandwichable.network.payload.CuttingBoardParticlesPayload;
import com.moigferdsrte.sandwichable.network.payload.MinecartServerSyncPayload;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class ClientNetworkRegistry {
    public static void clientInit() {
        ClientPlayNetworking.registerGlobalReceiver(MinecartServerSyncPayload.ID, (payload, ctx) -> {
            Entity e = ctx.client().player.getEntityWorld().getEntityById(payload.specId());
            ctx.client().execute(() -> {
                if(e instanceof SandwichTableMinecartEntity) {
                    ((SandwichTableMinecartEntity)e).readSandwichTableData(payload.nbt());
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(CuttingBoardParticlesPayload.ID, (payload, ctx) -> {
            ItemStack stack = payload.stack();
            int top = stack.getCount();
            int layers = payload.depth();
            BlockPos pos = payload.pos();
            Random random = ctx.client().world.getRandom();
            World world = MinecraftClient.getInstance().world;
            ctx.client().execute(() -> {
                for (int i = 0; i < layers; i++) {
                    for (int j = 0; j < 2 + random.nextInt(2); j++) {
                        double x = pos.getX() + 0.5 + ((random.nextDouble() - 0.5) / 3);
                        double y = pos.getY() + 0.094 + ((top - i) * 0.03124);
                        double z = pos.getZ() + 0.5 + ((random.nextDouble() - 0.5) / 3);
                        world.addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, stack), x, y, z, 0, (random.nextDouble() + 1.0) * 0.066, 0);
                    }
                }
            });
        });
    }
}
