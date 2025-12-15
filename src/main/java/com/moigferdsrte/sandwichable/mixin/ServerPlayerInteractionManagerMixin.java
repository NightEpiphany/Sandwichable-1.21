package com.moigferdsrte.sandwichable.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.moigferdsrte.sandwichable.blocks.extra.SneakInteractable;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
    @Definition(id = "copy", method = "Lnet/minecraft/item/ItemStack;copy()Lnet/minecraft/item/ItemStack;")
    @Expression("? = ?.copy()")
    @ModifyVariable(
            method = "interactBlock",
            at = @At(value = "MIXINEXTRAS:EXPRESSION", shift = At.Shift.AFTER),
            index = 9
    )
    private boolean sandwichable$modifyBlockInteractionCondition(boolean old, ServerPlayerEntity player, World world, ItemStack stack, Hand hand, BlockHitResult hit) {
        BlockState state = world.getBlockState(hit.getBlockPos());
        if (state.getBlock() instanceof SneakInteractable) {
            return !player.getMainHandStack().isEmpty() && player.shouldCancelInteraction();
        }
        return old;
    }
}
