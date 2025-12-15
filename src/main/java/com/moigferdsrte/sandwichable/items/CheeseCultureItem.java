package com.moigferdsrte.sandwichable.items;

import com.moigferdsrte.sandwichable.items.extra.BottleCrateStorable;
import com.moigferdsrte.sandwichable.items.extra.CheeseType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;

import static com.moigferdsrte.sandwichable.components.SandwichableDataComponent.CHEESE_CULTURE_USAGE;

public class CheeseCultureItem extends InfoTooltipItem implements BottleCrateStorable {
    private final CheeseType type;
    private final int craftAmount;
    private final float growthChance;

    public CheeseCultureItem(CheeseType type, int craftAmount, float growthChance, Settings settings) {
        super(settings);
        this.type = type;
        this.craftAmount = craftAmount;
        this.growthChance = growthChance;
    }

    @Override
    public void onCraftByPlayer(ItemStack stack, World world, PlayerEntity player) {
        super.onCraftByPlayer(stack, world, player);
        fill(stack, craftAmount);
    }

    public ItemStack fill(ItemStack stack, int amount) {
        if(stack.getItem() == this) {
            int old = stack.getOrDefault(CHEESE_CULTURE_USAGE, 0);
            stack.set(CHEESE_CULTURE_USAGE, Math.min(Math.max(0, old + amount), 10));
        } else if (stack.getItem() == Items.GLASS_BOTTLE) {
            stack = new ItemStack(this, 1);
            stack.set(CHEESE_CULTURE_USAGE, Math.min(Math.max(0, amount), 10));
        }
        return stack;
    }

    public ItemStack deplete(ItemStack stack, int amount) {
        if(stack.getItem() == this) {
            int old = stack.getOrDefault(CHEESE_CULTURE_USAGE, 0);
            int newA = Math.min(Math.max(0, old - amount), 10);
            if(newA == 0) stack = new ItemStack(Items.GLASS_BOTTLE);
            else stack.set(CHEESE_CULTURE_USAGE, newA);
        }
        return stack;
    }

    @Override
    public ItemStack getDefaultStack() {
        var stack = super.getDefaultStack();
        stack.set(CHEESE_CULTURE_USAGE, 10);
        return stack;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType types) {
        tooltip.add(Text.translatable("cheese.type."+ type.toString()).formatted(Formatting.BLUE));
        int uses = stack.getOrDefault(CHEESE_CULTURE_USAGE, 0);
        tooltip.add(Text.translatable("cheese_culture_bottle.tooltip.uses", uses).formatted(Formatting.DARK_GRAY));
        super.appendTooltip(stack, context, tooltip, types);
    }

    public CheeseType getCheeseType() {
        return type;
    }

    @Override
    public String getTranslationKey() {
        return "item.sandwichable.cheese_culture_bottle";
    }

    @Override
    public ItemStack bottleCrateRandomTick(Inventory inventory, ItemStack stack, Random random) {
        return random.nextFloat() <= growthChance ? fill(stack, 1) : stack;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        int uses = stack.getOrDefault(CHEESE_CULTURE_USAGE, 0);
        return (int)(14 * ((float)uses / 10));
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        int uses = stack.getOrDefault(CHEESE_CULTURE_USAGE, 0);
        return uses == 1 ? 0xff0000 : uses <= 3 ? 0x5465ff : 0x0099ff;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        int uses = stack.getOrDefault(CHEESE_CULTURE_USAGE, 0);
        return uses < 10 && uses != 0;
    }
}
