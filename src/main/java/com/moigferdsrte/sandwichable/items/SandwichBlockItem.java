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
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

import static net.minecraft.component.DataComponentTypes.*;

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
    public ItemStack finishUsing(ItemStack iStack, World world, LivingEntity user) {
        ItemStack stack = iStack.copy();
        if(stack.contains(BLOCK_ENTITY_DATA)) {
            NbtCompound tag = stack.get(BLOCK_ENTITY_DATA).copyNbt();
            cache.setFromNbt(tag, world.getRegistryManager());
            ItemStack finishStack;
            ItemCooldownManager cooldownManager = null;
            if(user instanceof PlayerEntity player) cooldownManager = player.getItemCooldownManager();
            for(int i = 0; i < cache.getSize(); i++) {
                ItemStack food = cache.getFoodList().get(i);
                if (food.contains(CUSTOM_DATA)) {
                    var custom = food.get(CUSTOM_DATA).copyNbt();
                    if (custom.contains("potionEffects", NbtElement.COMPOUND_TYPE)) {
                        var potion = custom.getCompound("potionEffects");
                        extractEffect(user, potion);
                    }else if (custom.contains("stewEffects", NbtElement.COMPOUND_TYPE)) {
                        var potion = custom.getCompound("stewEffects");
                        extractEffect(user, potion);
                    }
                }
                if(food.contains(FOOD)) {
                    finishStack = food.getItem().finishUsing(food, world, user);
                    if(user instanceof PlayerEntity player) {
                        if(!(player.isCreative() && !finishStack.getItem().equals(Items.AIR))) {
                            player.giveItemStack(finishStack);
                        }
                        if(cooldownManager != null && cooldownManager.isCoolingDown(food.getItem())) {
                            cooldownManager.set(this, 20);
                        }
                    }
                    if (food.contains(FOOD))
                        user.eatFood(world, food, food.get(FOOD));
                }
            }
        }
        return super.finishUsing(stack, world, user);
    }

    private void extractEffect(LivingEntity user, NbtCompound potion) {
        if (potion.contains("effect2")) {
            String ins2 = potion.getString("effect2");
            Identifier effectId = Identifier.tryParse(ins2);
            StatusEffect potions = Registries.STATUS_EFFECT.get(effectId);
            if (potions != null) {
                RegistryKey<StatusEffect> key = RegistryKey.of(RegistryKeys.STATUS_EFFECT, effectId);
                Registries.STATUS_EFFECT.getEntry(key).ifPresent(entry -> user.addStatusEffect(new StatusEffectInstance(entry, potion.getInt("duration2"), potion.getInt("amplifier2"))));
            }
        }
        String ins = potion.getString("effect");
        Identifier effectId = Identifier.tryParse(ins);
        StatusEffect potions = Registries.STATUS_EFFECT.get(effectId);
        if (potions != null) {
            RegistryKey<StatusEffect> key = RegistryKey.of(RegistryKeys.STATUS_EFFECT, effectId);
            Registries.STATUS_EFFECT.getEntry(key).ifPresent(entry -> user.addStatusEffect(new StatusEffectInstance(entry, potion.getInt("duration"), potion.getInt("amplifier"))));
        }
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
