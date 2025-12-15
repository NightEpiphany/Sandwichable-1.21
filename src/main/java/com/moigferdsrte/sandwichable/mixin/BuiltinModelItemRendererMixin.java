package com.moigferdsrte.sandwichable.mixin;

import com.moigferdsrte.sandwichable.registry.BlocksRegistry;
import com.moigferdsrte.sandwichable.util.Sandwich;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.component.DataComponentTypes.BLOCK_ENTITY_DATA;

@Environment(EnvType.CLIENT)
@Mixin(BuiltinModelItemRenderer.class)
public class BuiltinModelItemRendererMixin {
    @Unique
    private static final Sandwich cache = new Sandwich();

    @Unique
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/item/BlockItem;getBlock()Lnet/minecraft/block/Block;", shift = At.Shift.AFTER), method = "render")
    private void sandwichable$renderSandwichGui(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
        if(stack.getItem() == (BlocksRegistry.SANDWICH).asItem()) {
            matrices.push();
            if(stack.contains(BLOCK_ENTITY_DATA)) {
                NbtCompound tag = stack.get(BLOCK_ENTITY_DATA).copyNbt();
                matrices.translate(0.5, 0.017, 0.4);
                cache.setFromNbt(tag, mc.world.getRegistryManager());
                cache.render(matrices, vertexConsumers, light, overlay);
            } else {
                matrices.translate(0.5, 0.017, 0.4);
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((90)));
                MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(Items.BARRIER), ModelTransformationMode.GROUND, light, overlay, matrices, vertexConsumers, null, 204874579);
            }
            matrices.pop();
        }
    }
}
