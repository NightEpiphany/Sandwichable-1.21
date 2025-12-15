package com.moigferdsrte.sandwichable.items;

import com.moigferdsrte.sandwichable.items.extra.CheeseType;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public class CheeseItem extends InfoTooltipItem {

    private final CheeseType type;
    private final boolean isSlice;

    public CheeseItem(CheeseType type, boolean isSlice) {
        super(new Item.Settings().food(isSlice ? CheeseItem.sliceComponent(type) : CheeseItem.wheelComponent(type)));
        this.type = type;
        this.isSlice = isSlice;
    }

    private static FoodComponent wheelComponent(CheeseType type) {
        return new FoodComponent.Builder().nutrition(type.hunger * 3).saturationModifier(type.saturation).statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 420, 5), 1.0F).build();
    }
    private static FoodComponent sliceComponent(CheeseType type) {
        return new FoodComponent.Builder().nutrition(type.hunger).saturationModifier(type.saturation).snack().build();
    }

    public CheeseType getType() {
        return type;
    }

    @Override
    public String getTranslationKey() {
        return "item.sandwichable.cheese" + ( isSlice ? "_slice" : "_wheel" );
    }

    public boolean isSlice() {
        return isSlice;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType types) {
        tooltip.add(Text.translatable("cheese.type."+type.toString()).formatted(Formatting.BLUE));
        LocalDate date = LocalDate.now();
        if(date.getMonth() == Month.APRIL && date.getDayOfMonth() == 1) {
            tooltip.add(Text.translatable("cheese.type.dairy_free").formatted(Formatting.AQUA));
        }
        super.appendTooltip(stack, context, tooltip, types);
    }
}
