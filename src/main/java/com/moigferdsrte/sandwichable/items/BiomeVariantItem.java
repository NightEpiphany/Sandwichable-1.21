package com.moigferdsrte.sandwichable.items;

import com.moigferdsrte.sandwichable.util.Util;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.component.DataComponentTypes.CUSTOM_DATA;

public class BiomeVariantItem extends InfoTooltipItem {
    public BiomeVariantItem(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName(ItemStack stack) {
        var biomeId = getBiomeId(stack);
        if (biomeId != null) {
            return Text.translatable(this.getTranslationKey() + ".biome", Util.biomeName(biomeId));
        }

        return super.getName(stack);
    }

    public static @Nullable Identifier getBiomeId(ItemStack stack) {
        if (stack.contains(CUSTOM_DATA)) {
            return Identifier.tryParse(stack.get(CUSTOM_DATA).copyNbt().getString("biome"));
        }

        return null;
    }

    public static void setBiome(ItemStack stack, RegistryEntry<Biome> biome) {
        biome.getKey().ifPresent(key ->
                NbtComponent.set(CUSTOM_DATA, stack, nbt -> nbt.putString("biome", key.getValue().toString())));
    }

    public static @Nullable RegistryEntry<Biome> getBiome(@Nullable World world, ItemStack stack) {
        if (stack.contains(CUSTOM_DATA) && world != null) {
            var registry = world.getRegistryManager().get(RegistryKeys.BIOME);
            var id = getBiomeId(stack);

            if (id != null) {
                var entry = registry.getEntry(RegistryKey.of(RegistryKeys.BIOME, id));
                if (entry.isPresent()) {
                    return entry.get();
                }
            }
        }

        return null;
    }

    public static void copyBiome(ItemStack from, ItemStack to) {
        if (from.contains(CUSTOM_DATA) && from.get(CUSTOM_DATA).copyNbt().contains("biome")) {
            NbtComponent.set(CUSTOM_DATA, to, nbt -> nbt.putString("biome", from.get(CUSTOM_DATA).copyNbt().getString("biome")));
        }
    }

    @Environment(EnvType.CLIENT)
    private static World getClientWorld() {
        return MinecraftClient.getInstance().world;
    }
}
