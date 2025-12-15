package com.moigferdsrte.sandwichable.items;

import com.moigferdsrte.sandwichable.items.spread.SpreadType;
import com.moigferdsrte.sandwichable.util.SpreadRegistry;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import static net.minecraft.component.DataComponentTypes.CUSTOM_DATA;

public class SpreadItem extends Item {

    private final SpreadType type;

    public SpreadItem(SpreadType type) {
        super(new Settings().food(new FoodComponent.Builder().build()));
        this.type = type;
    }

    public SpreadType getType() {
        return type;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if(
                user instanceof PlayerEntity
                && stack.contains(CUSTOM_DATA)
                && stack.get(CUSTOM_DATA).copyNbt().contains("spreadType")
        ) {
            SpreadType type = SpreadRegistry.INSTANCE.fromString(stack.get(CUSTOM_DATA).copyNbt().getString("spreadType"));
            if(!((PlayerEntity)user).isCreative()) ((PlayerEntity)user).getHungerManager().add(type.getHunger(), type.getSaturationModifier());
            for(StatusEffectInstance effect : type.getStatusEffects(stack)) {
                user.addStatusEffect(effect);
            }
            type.finishUsing(stack, world, user);
        }
        return super.finishUsing(stack, world, user);
    }

    @Override
    public void onCraftByPlayer(ItemStack stack, World world, PlayerEntity player) {
        super.onCraftByPlayer(stack, world, player);
        if (!stack.contains(CUSTOM_DATA))
            NbtComponent.set(CUSTOM_DATA, stack, nbtCompound -> nbtCompound.putString("spreadType", "null"));
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        if(stack.contains(CUSTOM_DATA)) {
            NbtCompound nbtCompound = stack.get(CUSTOM_DATA).copyNbt();
            if(nbtCompound.getString("spreadType") != null) {
                String type = nbtCompound.getString("spreadType");
                if(SpreadRegistry.INSTANCE.fromString(type) != null) {
                    return SpreadRegistry.INSTANCE.fromString(type).getTranslationKey(type, stack);
                }
            }
        }
        return super.getTranslationKey(stack);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        if(stack.contains(CUSTOM_DATA)) {
            NbtCompound nbtCompound = stack.get(CUSTOM_DATA).copyNbt();
            if(nbtCompound.getString("spreadType") != null) {
                String type = nbtCompound.getString("spreadType");
                if(SpreadRegistry.INSTANCE.fromString(type) != null) {
                    return SpreadRegistry.INSTANCE.fromString(type).hasGlint(stack);
                }
            }
        }
        return super.hasGlint(stack);
    }
}
