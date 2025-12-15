package com.moigferdsrte.sandwichable.mixin;

import com.moigferdsrte.sandwichable.registry.BlocksRegistry;
import com.moigferdsrte.sandwichable.util.Util;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin extends Entity {
    @Unique
    private static final  RegistryKey<LootTable> BRINE_LOOT_FISHING = RegistryKey.of(RegistryKeys.LOOT_TABLE, Util.id("gameplay/brine_fishing"));

    public FishingBobberEntityMixin(EntityType<?> type, World world) {
        super(type, world);
        throw new AssertionError("accessed dummy constructor");
    }

    @ModifyVariable(method = "use", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/registry/ReloadableRegistries$Lookup;getLootTable(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/loot/LootTable;", shift = At.Shift.AFTER, ordinal = 0), index = 5)
    public LootTable sandwichable$changeLootTable(LootTable value) {
        BlockState state = this.getWorld().getBlockState(this.getBlockPos());
        System.out.println(state);
        if(state.isOf(BlocksRegistry.PICKLE_BRINE)) {
            return this.getWorld().getServer().getReloadableRegistries().getLootTable(BRINE_LOOT_FISHING);
        }
        return value;
    }
}
