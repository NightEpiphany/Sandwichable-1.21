package com.moigferdsrte.sandwichable.mixin;

import com.moigferdsrte.sandwichable.util.Util;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.ModelIdentifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin {
    @Shadow protected abstract void loadItemModel(ModelIdentifier id);

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/ModelLoader;loadItemModel(Lnet/minecraft/client/util/ModelIdentifier;)V", ordinal = 1, shift = At.Shift.AFTER))
    public void addPlate(CallbackInfo ci) {
        this.loadItemModel(ModelIdentifier.ofInventoryVariant(Util.id("spread_bread_loaf")));
        this.loadItemModel(ModelIdentifier.ofInventoryVariant(Util.id("spread")));
    }
}
