package com.moigferdsrte.sandwichable.advancement;

import com.moigferdsrte.sandwichable.util.Sandwich;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static net.minecraft.component.DataComponentTypes.BLOCK_ENTITY_DATA;

public class CollectSandwichCriterion extends AbstractCriterion<CollectSandwichCriterion.Conditions> {
    private static final Sandwich cache = new Sandwich();

    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, ItemStack sandwich) {
        if (sandwich.contains(BLOCK_ENTITY_DATA)) {
            cache.addFromNbt(sandwich.get(BLOCK_ENTITY_DATA).copyNbt(), player.getWorld().getRegistryManager());
            this.trigger(player, conditions -> conditions.test(cache));
        }
    }

    public record Conditions(Optional<LootContextPredicate> player, Optional<ItemPredicate> foods) implements AbstractCriterion.Conditions {

        public static final Codec<CollectSandwichCriterion.Conditions> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(CollectSandwichCriterion.Conditions::player),
                                ItemPredicate.CODEC.optionalFieldOf("foods").forGetter(CollectSandwichCriterion.Conditions::foods)

                        )
                        .apply(instance, CollectSandwichCriterion.Conditions::new)
        );

        public boolean test(Sandwich sandwich) {
            AtomicBoolean pass = new AtomicBoolean(false);
            this.foods().ifPresent(predicate -> {
                for (ItemStack stack : sandwich.getFoodList()) {
                    if (predicate.test(stack)) pass.set(true);
                }
            });
            return pass.get();
        }
    }
}
