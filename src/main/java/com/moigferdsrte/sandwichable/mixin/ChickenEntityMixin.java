package com.moigferdsrte.sandwichable.mixin;

import com.moigferdsrte.sandwichable.items.SandwichGreenSeedItem;
import com.moigferdsrte.sandwichable.util.Util;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChickenEntity.class)
public class ChickenEntityMixin {
    @Inject(method = "isBreedingItem", at = @At("HEAD"), cancellable = true)
    public void isBreedingItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (Registries.ITEM.getId(stack.getItem()).getNamespace().equals(Util.MOD_ID)) {
            cir.setReturnValue(stack.getItem() instanceof SandwichGreenSeedItem);
        }
    }
}
