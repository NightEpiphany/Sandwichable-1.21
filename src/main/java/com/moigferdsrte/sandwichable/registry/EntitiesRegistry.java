package com.moigferdsrte.sandwichable.registry;

import com.moigferdsrte.sandwichable.entity.SandwichTableMinecartEntity;
import com.moigferdsrte.sandwichable.util.Util;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class EntitiesRegistry {
    public static final EntityType<SandwichTableMinecartEntity> SANDWICH_TABLE_MINECART = Registry.register(
            Registries.ENTITY_TYPE,
            Util.id("sandwich_table_minecart"),
            EntityType.Builder.<SandwichTableMinecartEntity>create(SandwichTableMinecartEntity::new, SpawnGroup.MISC).dimensions(0.98F, 0.7F).build()
    );

    public static void init() {}
}
