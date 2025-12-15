package com.moigferdsrte.sandwichable.items;

import com.moigferdsrte.sandwichable.items.extra.CheeseType;
import com.moigferdsrte.sandwichable.util.CheeseRegistry;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.List;

import static net.minecraft.component.DataComponentTypes.CUSTOM_DATA;

public class FermentingMilkBucketItem extends InfoTooltipItem {

    public FermentingMilkBucketItem(Settings settings) {
        super(settings.food(new FoodComponent.Builder().saturationModifier(3).saturationModifier(0.9F).alwaysEdible().build()).recipeRemainder(Items.BUCKET));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if(stack.contains(CUSTOM_DATA)) {
            NbtCompound nbt = stack.get(CUSTOM_DATA).copyNbt();
            if(nbt.contains("bucketData")) {
                NbtCompound tag = nbt.getCompound("bucketData");
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, tag.getInt("percentFermented") * 4, 5));
            }
        }
        return user instanceof PlayerEntity && ((PlayerEntity)user).getAbilities().creativeMode ? super.finishUsing(stack, world, user) : new ItemStack(Items.BUCKET);
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    public SoundEvent getDrinkSound() {
        return SoundEvents.ENTITY_GENERIC_DRINK;
    }

    public SoundEvent getEatSound() {
        return SoundEvents.ENTITY_GENERIC_DRINK;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType types) {
        if(stack.contains(CUSTOM_DATA)) {
            NbtCompound nbt = stack.get(CUSTOM_DATA).copyNbt();
            if(nbt.getCompound("bucketData") != null) {
                NbtCompound tag = nbt.getCompound("bucketData").copy();
                int pct;
                CheeseType type;
                pct = tag.getInt("percentFermented");
                type = CheeseRegistry.INSTANCE.basinContentFromString(tag.getString("basinContent")).getCheeseType();
                tooltip.add(Text.translatable("fermenting_milk_bucket.tooltip.pct_fermented", pct).formatted(Formatting.BLUE));
                if (type != null) {
                    tooltip.add(Text.translatable("cheese.type." + type.toString()).formatted(Formatting.BLUE));
                }
            }
        } else {
            tooltip.add(Text.translatable("fermenting_milk_bucket.tooltip.no_properties").formatted(Formatting.BLUE));
        }
        super.appendTooltip(stack, context, tooltip, types);
    }
}
