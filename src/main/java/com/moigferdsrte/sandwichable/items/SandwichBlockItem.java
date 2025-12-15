package com.moigferdsrte.sandwichable.items;

import com.moigferdsrte.sandwichable.config.SandwichableConfig;
import com.moigferdsrte.sandwichable.util.Sandwich;
import com.moigferdsrte.sandwichable.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

import static net.minecraft.component.DataComponentTypes.BLOCK_ENTITY_DATA;
import static net.minecraft.component.DataComponentTypes.FOOD;

public class SandwichBlockItem extends InfoTooltipBlockItem {

    private static final MinecraftClient mc = MinecraftClient.getInstance();

    private final Sandwich cache = new Sandwich();

    public SandwichBlockItem(Block block) {
        super(block, new Settings().food(new FoodComponent.Builder().build()).maxCount(1));
    }

    @Override
    protected boolean place(ItemPlacementContext context, BlockState state) {
        assert context.getPlayer() != null;
        if (context.getPlayer().isSneaking()) {
            return super.place(context, state);
        }
        return false;
    }

    public List<ItemStack> getFoodList(ItemStack stack, RegistryWrapper.WrapperLookup registries) {
        NbtCompound tag = stack.getOrDefault(BLOCK_ENTITY_DATA, NbtComponent.DEFAULT).copyNbt();
        cache.setFromNbt(tag, registries);
        return cache.getFoodList();
    }

    public Sandwich.DisplayValues getDisplayValues(ItemStack stack, RegistryWrapper.WrapperLookup registries) {
        NbtCompound tag = stack.getOrDefault(BLOCK_ENTITY_DATA, NbtComponent.DEFAULT).copyNbt();
        cache.setFromNbt(tag, registries);
        if(!tag.contains("DisplayValues")) {
            cache.putDisplayValues(tag);
        }
        return Sandwich.getDisplayValues(tag.getCompound("DisplayValues"));
    }

    @Override
    public Text getName(ItemStack stack) {
        if(stack.contains(BLOCK_ENTITY_DATA)) {
            NbtCompound tag = stack.get(BLOCK_ENTITY_DATA).copyNbt();
            cache.setFromNbt(tag, mc.world.getRegistryManager());
            int size = cache.getSize();
            boolean hacked = false;
            for(ItemStack food : cache.getFoodList()) {
                if(!food.contains(FOOD) && !food.isEmpty()) { hacked = true; }
            }
            if(size <= 2 && hacked) {
                return Text.translatable("block.sandwichable.hackedzandwich");
            } else if(size >= 127 && hacked) {
                return Text.translatable("block.sandwichable.hackedsandwich").formatted(Formatting.AQUA);
            } else if(size >= 127) {
                return ((MutableText)super.getName(stack)).formatted(Formatting.AQUA);
            } else if(size <= 2) {
                return Text.translatable("block.sandwichable.zandwich");
            } else if (hacked) {
                return Text.translatable("block.sandwichable.hackedsandwich");
            }
        }
        return super.getName(stack);
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        SandwichableConfig config = Util.getConfig();
        return config.baseSandwichEatTime + (config.slowEatingLargeSandwiches ? getFoodListSize(stack) : 0);
    }

    public int getFoodListSize(ItemStack stack) {
        cache.setFromNbt(stack.getOrDefault(BLOCK_ENTITY_DATA, NbtComponent.DEFAULT).copyNbt(), mc.world.getRegistryManager());
        return cache.getSize();
    }

    @Override
    public ItemStack finishUsing(ItemStack istack, World world, LivingEntity user) {
        ItemStack stack = istack.copy();
        if(stack.contains(BLOCK_ENTITY_DATA)) {
            NbtCompound tag = stack.get(BLOCK_ENTITY_DATA).copyNbt();
            cache.setFromNbt(tag, world.getRegistryManager());
            ItemStack finishStack;
            ItemCooldownManager cooldownManager = null;
            if(user instanceof PlayerEntity) cooldownManager = ((PlayerEntity)user).getItemCooldownManager();
            for(int i = 0; i < cache.getSize(); i++) {
                ItemStack food = cache.getFoodList().get(i);
                if(food.contains(FOOD)) {
                    finishStack = food.getItem().finishUsing(food, world, user);
                    if(user instanceof PlayerEntity) {
                        if(!((PlayerEntity)user).isCreative() && !finishStack.getItem().equals(Items.AIR)) {
                            ((PlayerEntity)user).giveItemStack(finishStack);
                        }
                        if(cooldownManager != null && cooldownManager.isCoolingDown(food.getItem())) {
                            cooldownManager.set(this, 20);
                        }
                    }
                    user.eatFood(world, food, food.get(FOOD));
                }
            }
        }
        return super.finishUsing(stack, world, user);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        cache.setFromNbt(stack.getOrDefault(BLOCK_ENTITY_DATA, NbtComponent.DEFAULT).copyNbt(), mc.world.getRegistryManager());
        int size = cache.getSize();
        List<ItemStack> foods = cache.getFoodList();
        int i = 0; while(i < size && i < 5) {
            if(i < 4) {
                tooltip.add(((MutableText)foods.get(i).getName()).formatted(Formatting.BLUE));
            } else {
                tooltip.add(Text.translatable("sandwich.tooltip.ellipsis").formatted(Formatting.BLUE));
            }
            i++;
        }
        boolean hacked = false;
        for(int it = 0; it < foods.size() && !hacked; it++) {
            if(!foods.get(it).contains(FOOD)) {
                hacked = true;
            }
        }
        if(size <= 2) {
            tooltip.add(Text.translatable("sandwich.tooltip.zandwich").formatted(Formatting.BLUE));
        }
        if(size >= 127) {
            tooltip.add(Text.translatable("sandwich.tooltip.bigsandwich").formatted(Formatting.BLUE));
        }
        if(hacked) {
            tooltip.add(Text.translatable("sandwich.tooltip.hacked").formatted(Formatting.DARK_PURPLE));
        }
        super.appendTooltip(stack, context, tooltip, type);
    }
}
