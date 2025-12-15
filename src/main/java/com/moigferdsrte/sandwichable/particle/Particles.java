package com.moigferdsrte.sandwichable.particle;

import com.moigferdsrte.sandwichable.util.BlockLeakParticleDuck;
import com.moigferdsrte.sandwichable.util.Util;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class Particles {
    public static final SimpleParticleType DRIPPING_BRINE = Registry.register(Registries.PARTICLE_TYPE, Util.id("dripping_brine"), FabricParticleTypes.simple());
    public static final SimpleParticleType FALLING_BRINE = Registry.register(Registries.PARTICLE_TYPE, Util.id("falling_brine"), FabricParticleTypes.simple());
    public static final SimpleParticleType BRINE_SPLASH = Registry.register(Registries.PARTICLE_TYPE, Util.id("brine_splash"), FabricParticleTypes.simple());
    public static final SimpleParticleType BRINE_BUBBLE = Registry.register(Registries.PARTICLE_TYPE, Util.id("brine_bubble"), FabricParticleTypes.simple());
    public static final SimpleParticleType SMALL_BRINE_BUBBLE = Registry.register(Registries.PARTICLE_TYPE, Util.id("small_brine_bubble"), FabricParticleTypes.simple());

    public static void init() {
        ParticleFactoryRegistry.getInstance().register(FALLING_BRINE, withSprite(Particles::createFallingBrine));
        ParticleFactoryRegistry.getInstance().register(DRIPPING_BRINE, withSprite(Particles::createDrippingBrine));
        ParticleFactoryRegistry.getInstance().register(BRINE_SPLASH, WaterSplashParticle.SplashFactory::new);
        ParticleFactoryRegistry.getInstance().register(BRINE_BUBBLE, WaterBubbleParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(SMALL_BRINE_BUBBLE, BubbleColumnUpParticle.Factory::new);
    }

    public static SpriteBillboardParticle createFallingBrine(SimpleParticleType type, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        var particle = BlockLeakParticle.createFallingWater(type, world, x, y, z, velocityX, velocityY, velocityZ);
        if (particle instanceof BlockLeakParticle leak) {
            leak.setColor(0.25f, 1.0f, 0.4f);
            ((BlockLeakParticleDuck)leak).sandwichable_1_21_1$setNextParticle(BRINE_SPLASH);
        }
        return particle;
    }

    public static SpriteBillboardParticle createDrippingBrine(SimpleParticleType type, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        var particle = BlockLeakParticle.createDrippingWater(type, world, x, y, z, velocityX, velocityY, velocityZ);
        if (particle instanceof BlockLeakParticle leak) {
            leak.setColor(0.25f, 1.0f, 0.4f);
            ((BlockLeakParticleDuck)leak).sandwichable_1_21_1$setNextParticle(FALLING_BRINE);
        }
        return particle;
    }

    public static <E extends ParticleEffect> ParticleFactoryRegistry.PendingParticleFactory<E> withSprite(ParticleFactory.BlockLeakParticleFactory<E> factory) {
        return sprites -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
            var particle = factory.createParticle(parameters, world, x, y, z, velocityX, velocityY, velocityZ);
            if (particle != null) {
                particle.setSprite(sprites);
            }
            return particle;
        };
    }
}
