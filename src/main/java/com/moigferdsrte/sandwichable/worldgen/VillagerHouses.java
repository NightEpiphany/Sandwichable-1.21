package com.moigferdsrte.sandwichable.worldgen;

import com.google.common.collect.Lists;
import com.moigferdsrte.sandwichable.util.Util;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;

public class VillagerHouses {

    public static final Logger LOGGER = LoggerFactory.getLogger(Util.MOD_ID);

    private static final Identifier PLAINS = Identifier.tryParse("minecraft:village/plains/houses");
    private static final Identifier SNOWY = Identifier.tryParse("minecraft:village/snowy/houses");
    private static final Identifier SAVANNA = Identifier.tryParse("minecraft:village/savanna/houses");
    private static final Identifier DESERT = Identifier.tryParse("minecraft:village/desert/houses");
    private static final Identifier TAIGA = Identifier.tryParse("minecraft:village/taiga/houses");

    private static boolean initializationSuccessful = false;
    private static boolean initializationAttempted = false;

    public static void register() {
        ServerLifecycleEvents.SERVER_STARTING.register(minecraftServer -> {
            var registryAccess = minecraftServer.getRegistryManager();

            addBuildingToPool(registryAccess, PLAINS, "village/plains/houses/plains_sandwich_stand", 4);
            addBuildingToPool(registryAccess, SNOWY, "village/snowy/houses/snowy_sandwich_stand", 4);
            addBuildingToPool(registryAccess, SAVANNA, "village/savanna/houses/savanna_sandwich_stand", 4);
            addBuildingToPool(registryAccess, DESERT, "village/desert/houses/desert_sandwich_stand", 4);
            addBuildingToPool(registryAccess, TAIGA, "village/taiga/houses/taiga_sandwich_stand", 4);
        });
    }

    public static void addBuildingToPool(DynamicRegistryManager registryAccess, Identifier poolId, String structId, int weight) {

        if (initializationAttempted && !initializationSuccessful) {
            return;
        }

        try {
            var templatePools = registryAccess.getOptional(RegistryKeys.TEMPLATE_POOL);
            if (templatePools.isEmpty()) {
                LOGGER.warn("Template pools registry is empty for pool: {}", poolId);
                return;
            }
            var processorLists = registryAccess.getOptional(RegistryKeys.PROCESSOR_LIST);
            if (processorLists.isEmpty()) {
                LOGGER.warn("Processor lists registry is empty for pool: {}", poolId);
                return;
            }
            StructurePool pool = templatePools.get().get(poolId);
            if (pool == null) {
                LOGGER.warn("Structure pool not found: {}", poolId);
                return;
            }


            List<Pair<StructurePoolElement, Integer>> newRawTemplates = Lists.newArrayList(pool.elementCounts);

            String[] possibleFieldNames = {"rawTemplates", "field_16864", "elementCounts"};
            boolean fieldFound = false;

            for (String fieldName : possibleFieldNames) {
                try {
                    Field rawTemplatesField = StructurePool.class.getDeclaredField(fieldName);
                    rawTemplatesField.setAccessible(true);
                    rawTemplatesField.set(pool, newRawTemplates);
                    fieldFound = true;
                    LOGGER.debug("Successfully updated field '{}' for pool: {}", fieldName, poolId);
                    initializationSuccessful = true;
                    break;
                } catch (NoSuchFieldException e) {
                    LOGGER.debug("Field '{}' not found, trying next possible field name", fieldName);
                } catch (Exception e) {
                    LOGGER.warn("Failed to set field '{}' for pool {}: {}", fieldName, poolId, e.getMessage());
                }
            }

            if (!fieldFound) {
                LOGGER.error("Failed to find any valid field for rawTemplates in StructureTemplatePool. Tried: {}", String.join(", ", possibleFieldNames));
                initializationSuccessful = false;
                initializationAttempted = true;
            }

        } catch (Exception e) {
            LOGGER.error("Failed to add village structure to pool {}: {}", poolId, e.getMessage());
            throw new RuntimeException("Failed to add village structure", e);
        }
    }
}
