package com.moigferdsrte.sandwichable.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class ToastItemCriterion extends AbstractCriterion<ToastItemCriterion.Conditions> {

    public void trigger(ServerPlayerEntity player, ItemStack stack) {
        this.trigger(player, conditions -> conditions.test(stack));
    }

    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public record Conditions(Optional<LootContextPredicate> player, Optional<ItemPredicate> result) implements AbstractCriterion.Conditions {

        public static final Codec<ToastItemCriterion.Conditions> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(ToastItemCriterion.Conditions::player),
                                ItemPredicate.CODEC.optionalFieldOf("result").forGetter(ToastItemCriterion.Conditions::result)

                        )
                        .apply(instance, ToastItemCriterion.Conditions::new)
        );

        public boolean test(ItemStack stack) {
            return result.map(itemPredicate -> itemPredicate.test(stack)).orElse(true);
        }
    }
}
