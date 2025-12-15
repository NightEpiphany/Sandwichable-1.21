package com.moigferdsrte.sandwichable.items;

import com.moigferdsrte.sandwichable.blocks.entity.PickleJarBlockEntity;
import com.moigferdsrte.sandwichable.blocks.extra.PickleJarFluid;
import com.moigferdsrte.sandwichable.registry.BlocksRegistry;
import com.moigferdsrte.sandwichable.registry.ItemsRegistry;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

import static net.minecraft.component.DataComponentTypes.BLOCK_ENTITY_DATA;

public class PickleJarBlockItem extends InfoTooltipBlockItem {
    private final String tooltipKey;
    private final boolean isDefault;

    public PickleJarBlockItem(String tooltipKey, boolean isDefault, Settings settings) {
        super(BlocksRegistry.PICKLE_JAR, settings);
        this.tooltipKey = tooltipKey;
        this.isDefault = isDefault;
    }

    public String getTranslationKey() {
        return "block.sandwichable.pickle_jar";
    }

    public static ItemStack createFromBlockEntity(PickleJarBlockEntity entity) {
        NbtCompound tag = new NbtCompound();
        entity.writeNbt(tag, entity.getWorld().getRegistryManager());
        ItemStack stack = new ItemStack(ItemsRegistry.EMPTY_PICKLE_JAR, 1);
        PickleJarFluid fluid = PickleJarFluid.fromString(tag.getString("pickleJarFluid"));
        int numItems = tag.getInt("numItems");

        if(fluid == PickleJarFluid.WATER && numItems > 0) {
            stack = new ItemStack(ItemsRegistry.CUCUMBER_FILLED_PICKLE_JAR, 1);
        } else if(fluid == PickleJarFluid.WATER && numItems == 0) {
            stack = new ItemStack(ItemsRegistry.WATER_FILLED_PICKLE_JAR, 1);
        } else if(fluid == PickleJarFluid.PICKLING_BRINE) {
            stack = new ItemStack(ItemsRegistry.PICKLING_PICKLE_JAR, 1);
        } else if(fluid == PickleJarFluid.PICKLED_BRINE) {
            stack = new ItemStack(ItemsRegistry.PICKLE_FILLED_PICKLE_JAR, 1);
        }
        NbtComponent.set(BLOCK_ENTITY_DATA, stack, tag);

        return stack;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType types) {
        tooltip.add(Text.translatable("pickle_jar.tooltip.contents").formatted(Formatting.AQUA));
        if(stack.contains(BLOCK_ENTITY_DATA)) {
            tooltip.add(Text.translatable(this.tooltipKey).formatted(Formatting.BLUE));
            NbtCompound tag = stack.get(BLOCK_ENTITY_DATA).copyNbt();
            PickleJarFluid fluid = PickleJarFluid.fromString(tag.getString("pickleJarFluid"));
            int numItems = tag.getInt("numItems");
            int pickleProgress = tag.getInt("pickleProgress");

            if(fluid == PickleJarFluid.WATER && numItems > 0) {
                tooltip.add(Text.translatable("pickle_jar.tooltip.cucumber_ct", numItems).formatted(Formatting.BLUE));
            }
            else if((fluid == PickleJarFluid.PICKLED_BRINE || fluid == PickleJarFluid.PICKLING_BRINE) && numItems > 0) {
                tooltip.add(Text.translatable("pickle_jar.tooltip.pickle_ct", numItems).formatted(Formatting.BLUE));
            }
            if(fluid == PickleJarFluid.PICKLING_BRINE) {
                int pct = (int)(((float)pickleProgress/PickleJarBlockEntity.pickleTime)*100);
                tooltip.add(Text.translatable("pickle_jar.tooltip.pct_pickled", pct).formatted(Formatting.BLUE));
            }
        } else if(this.isDefault) {
            tooltip.add(Text.translatable(this.tooltipKey).formatted(Formatting.BLUE));
        } else {
            tooltip.add(Text.translatable("pickle_jar.content.null").formatted(Formatting.RED));
        }
        super.appendTooltip(stack, context, tooltip, types);
    }
}
