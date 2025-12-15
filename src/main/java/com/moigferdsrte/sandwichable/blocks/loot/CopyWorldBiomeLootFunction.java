package com.moigferdsrte.sandwichable.blocks.loot;

import com.moigferdsrte.sandwichable.Sandwichable;
import com.moigferdsrte.sandwichable.items.BiomeVariantItem;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class CopyWorldBiomeLootFunction extends ConditionalLootFunction {
    protected CopyWorldBiomeLootFunction(List<LootCondition> conditions) {
        super(conditions);
    }

    public static final MapCodec<CopyWorldBiomeLootFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance ->
                    addConditionsField(instance)
                            .apply(instance, CopyWorldBiomeLootFunction::new)
    );

    @Override
    protected ItemStack process(ItemStack stack, LootContext context) {
        if (context.hasParameter(LootContextParameters.ORIGIN)) {
            var origin = context.get(LootContextParameters.ORIGIN);
            BiomeVariantItem.setBiome(stack, context.getWorld().getBiome(BlockPos.ofFloored(origin)));
        }

        return stack;
    }

    @Override
    public LootFunctionType<? extends ConditionalLootFunction> getType() {
        return Sandwichable.COPY_WORLD_BIOME;
    }
}
