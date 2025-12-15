package com.moigferdsrte.sandwichable.items;

import com.moigferdsrte.sandwichable.config.SandwichableConfig;
import com.moigferdsrte.sandwichable.items.extra.AbstractKnifeItem;
import com.moigferdsrte.sandwichable.util.Util;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

import static com.moigferdsrte.sandwichable.components.SandwichableDataComponent.KITCHEN_KNIFE_SHARPNESS;

public class KitchenKnifeItem extends AbstractKnifeItem {
    public KitchenKnifeItem(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    public static int getSharpness(ItemStack knife) {
        if (!(knife.getItem() instanceof KitchenKnifeItem)) return 0;
        SharpnessData kData = knife.getOrDefault(KITCHEN_KNIFE_SHARPNESS, SharpnessData.DEFAULT);
        if (kData.sharpnessNotSet()) {
            SandwichableConfig.KitchenKnifeOption opt = Util.getConfig().getKnifeOption(knife.getItem());
            if (opt != null) {
                knife.set(KITCHEN_KNIFE_SHARPNESS, new SharpnessData(opt.sharpness, kData.maxSharpness));
                return opt.sharpness;
            }
        }
        return kData.sharpness;
    }

    public static int getMaxSharpness(ItemStack knife) {
        if (!(knife.getItem() instanceof KitchenKnifeItem)) return 0;
        SharpnessData kData = knife.getOrDefault(KITCHEN_KNIFE_SHARPNESS, SharpnessData.DEFAULT);
        if (kData.maxSharpnessNotSet()) {
            SandwichableConfig.KitchenKnifeOption opt = Util.getConfig().getKnifeOption(knife.getItem());
            if (opt != null) {
                knife.set(KITCHEN_KNIFE_SHARPNESS, new SharpnessData(kData.sharpness, opt.sharpness));
                return opt.sharpness;
            }
        }
        return kData.maxSharpness;
    }

    public static void setSharpness(ItemStack knife, int amount) {
        if (!(knife.getItem() instanceof KitchenKnifeItem)) return;
        SharpnessData kData = knife.getOrDefault(KITCHEN_KNIFE_SHARPNESS, SharpnessData.DEFAULT);
        SandwichableConfig.KitchenKnifeOption opt = Util.getConfig().getKnifeOption(knife.getItem());
        if (opt != null) knife.set(KITCHEN_KNIFE_SHARPNESS, new SharpnessData(MathHelper.clamp(amount, 0, getMaxSharpness(knife)),kData.maxSharpness));
    }

    public static float getSharpnessF(ItemStack knife) {
        return (float)getSharpness(knife) / getMaxSharpness(knife);
    }

    public static int getItemCutAmount(ItemStack knife) {
        SandwichableConfig.KitchenKnifeOption opt = Util.getConfig().getKnifeOption(knife.getItem());
        if (opt == null) return 0;
        if (!(knife.getItem() instanceof KitchenKnifeItem)) return opt.value;
        return itemCutAmountFrom(getSharpnessF(knife), opt.value);
    }

    private static int itemCutAmountFrom(float sharpness, int knifeValue) {
        return (int)Math.ceil(sharpness * knifeValue);
    }

    public static void processCut(ItemStack knife, int items) {
        if (!(knife.getItem() instanceof KitchenKnifeItem)) return;
        int decrement = (int)Math.ceil((float)items * 0.7);
        setSharpness(knife, getSharpness(knife) - decrement);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        SandwichableConfig.KitchenKnifeOption opt = Util.getConfig().getKnifeOption(stack.getItem());
        if (opt != null) {
            float sharpness = getSharpnessF(stack);
            tooltip.add(Text.translatable("kitchen_knife.tooltip.sharpness", Math.round(sharpness * 100)).formatted(Formatting.DARK_GRAY));
            int itemsCut = itemCutAmountFrom(sharpness, opt.value);
            tooltip.add(Text.translatable("kitchen_knife.tooltip.items_cut" + (itemsCut == 1 ? "_singular" : ""), itemsCut).formatted(Formatting.DARK_GRAY));
        }
        super.appendTooltip(stack, context, tooltip, type);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = super.getDefaultStack();
        getSharpness(stack);
        return stack;
    }

    @Override
    public void onCraftByPlayer(ItemStack stack, World world, PlayerEntity player) {
        super.onCraftByPlayer(stack, world, player);
        getSharpness(stack);
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return (int)(14 * getSharpnessF(stack));
    }

    public static final int[] COLORS = {
        0x4b5e4e, 0x456b51, 0x448261, 0x449477, 0x43ba9a, 0x43f0cd
    };

    @Override
    public int getItemBarColor(ItemStack stack) {
        return COLORS[Math.round(getSharpnessF(stack) * (COLORS.length - 1))];
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return getSharpnessF(stack) < 1;
    }

    public record SharpnessData(int sharpness, int maxSharpness) {
        public static final SharpnessData DEFAULT = new SharpnessData(0, 0);
        public static final Codec<SharpnessData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("sharpness").forGetter(SharpnessData::sharpness),
                Codec.INT.fieldOf("maxSharpness").forGetter(SharpnessData::maxSharpness)
        ).apply(instance, SharpnessData::new));

        public static final PacketCodec<RegistryByteBuf, SharpnessData> PACKET_CODEC = new PacketCodec<>()
        {
            @Override
            public SharpnessData decode(RegistryByteBuf buf) {
                int sharpness = buf.readVarInt();
                int maxSharpness = buf.readVarInt();
                return new SharpnessData(sharpness, maxSharpness);
            }

            @Override
            public void encode(RegistryByteBuf buf, SharpnessData value) {
                buf.writeVarInt(value.sharpness);
                buf.writeVarInt(value.maxSharpness);
            }
        };

        public boolean sharpnessNotSet() {
            return sharpness == 0;
        }

        public boolean maxSharpnessNotSet() {
            return maxSharpness == 0;
        }
    }
}
