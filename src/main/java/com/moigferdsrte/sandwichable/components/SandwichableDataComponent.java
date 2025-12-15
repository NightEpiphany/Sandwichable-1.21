package com.moigferdsrte.sandwichable.components;

import com.moigferdsrte.sandwichable.items.KitchenKnifeItem;
import com.moigferdsrte.sandwichable.util.Util;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

public class SandwichableDataComponent {

    public static final ComponentType<Integer> CHEESE_CULTURE_USAGE = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(Util.MOD_ID, "cheese_culture_usage_data"),
            ComponentType.<Integer>builder()
                    .codec(Codecs.NONNEGATIVE_INT)
                    .packetCodec(PacketCodecs.VAR_INT)
                    .build()
    );

    public static final ComponentType<KitchenKnifeItem.SharpnessData> KITCHEN_KNIFE_SHARPNESS = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(Util.MOD_ID, "kitchen_knife_sharpness_data"),
            ComponentType.<KitchenKnifeItem.SharpnessData>builder()
                    .codec(KitchenKnifeItem.SharpnessData.CODEC)
                    .packetCodec(KitchenKnifeItem.SharpnessData.PACKET_CODEC)
                    .build()
    );


    public static void init() {

    }
}
