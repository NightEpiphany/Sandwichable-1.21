package com.moigferdsrte.sandwichable.util;

import com.moigferdsrte.sandwichable.Sandwichable;
import com.moigferdsrte.sandwichable.blocks.entity.BasinBlockEntity;
import com.moigferdsrte.sandwichable.blocks.entity.PickleJarBlockEntity;
import com.moigferdsrte.sandwichable.blocks.entity.SandwichTableBlockEntity;
import com.moigferdsrte.sandwichable.blocks.extra.BasinContent;
import com.moigferdsrte.sandwichable.blocks.extra.BasinContentType;
import com.moigferdsrte.sandwichable.blocks.extra.PickleJarFluid;
import com.moigferdsrte.sandwichable.entity.SandwichTableMinecartEntity;
import com.moigferdsrte.sandwichable.items.CheeseCultureItem;
import com.moigferdsrte.sandwichable.registry.BlocksRegistry;
import com.moigferdsrte.sandwichable.registry.ItemsRegistry;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.minecraft.component.DataComponentTypes.FOOD;

public class ExtraDispenserBehaviorRegistry {
    public static final Map<ItemConvertible, List<DispenserBehavior>> ENTRIES = new HashMap<>();

    public static void register(ItemConvertible item, DispenserBehavior behavior) {
        if(!ENTRIES.containsKey(item)) ENTRIES.put(item, new ArrayList<>());
        ENTRIES.get(item).add(behavior);
    }

    public static void initDefaults() {
        DispenserBehavior foodBehavior = new ItemDispenserBehavior() {
            @Override
            protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                BlockPos pos = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING));
                World world = pointer.world();
                Sandwich sandwich = null;
                Runnable sync = () -> {};
                if(world.getBlockEntity(pos) instanceof SandwichTableBlockEntity sandwichTableBlockEntity) {
                    sandwich = sandwichTableBlockEntity.getSandwich();
                    sync = () -> Util.sync((sandwichTableBlockEntity));
                } else {
                    List<SandwichTableMinecartEntity> list = pointer.world().getEntitiesByClass(SandwichTableMinecartEntity.class, new Box(pos), EntityPredicates.EXCEPT_SPECTATOR);
                    if(!list.isEmpty()) {
                        sandwich = list.get(0).getSandwich();
                        sync = list.get(0)::sync;
                    }
                }
                if(sandwich != null) {
                    if(!sandwich.hasBreadBottom() && !Sandwichable.isBread(stack)) return null;
                    ItemStack r = sandwich.tryAddTopFoodFrom(world, stack);
                    if(r != null) {
                        sync.run();
                        if(!r.isEmpty() && pointer.world().getBlockEntity(pointer.pos()) instanceof DispenserBlockEntity be) {
                            var a = be.addToFirstFreeSlot(r);
                            if(a.isEmpty()) return null;
                        }
                        return stack;
                    }
                }
                return null;
            }
        };
        Util.forEveryEntryEver(Registries.ITEM, item -> {
            if((item.getDefaultStack().contains(FOOD) || SpreadRegistry.INSTANCE.itemHasSpread(item)) && item.asItem() != BlocksRegistry.SANDWICH.asItem()) {
                register(item, foodBehavior);
            }
            if(item instanceof CheeseCultureItem) {
                register(item, (pointer, stack) -> {
                    BlockPos pos = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING));
                    ServerWorld world = pointer.world();
                    if(world.getBlockEntity(pos) instanceof BasinBlockEntity be) {
                        if(be.getContent().getContentType() == BasinContentType.MILK) {
                            return be.addCheeseCulture(stack);
                        }
                    }
                    return null;
                });
            }
        });
        ItemDispenserBehavior milkBehavior = new ItemDispenserBehavior() {
            @Override
            protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                BlockPos pos = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING));
                ServerWorld world = pointer.world();
                if(world.getBlockEntity(pos) instanceof BasinBlockEntity be) {
                    if(be.getContent() == BasinContent.AIR) {
                        return be.insertMilk(stack);
                    }
                }
                return null;
            }
        };
        register(Items.MILK_BUCKET, milkBehavior);
        register(ItemsRegistry.FERMENTING_MILK_BUCKET, milkBehavior);
        register(Items.BUCKET, (pointer, stack) -> {
            BlockPos pos = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING));
            ServerWorld world = pointer.world();
            if(world.getBlockEntity(pos) instanceof BasinBlockEntity be) {
                if(be.getContent().getContentType().isLiquid) {
                    return be.extractMilk();
                }
            }
            return null;
        });
        register(Items.BUCKET, (pointer, stack) -> {
            BlockPos pos = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING));
            ServerWorld world = pointer.world();
            if(world.getBlockEntity(pos) instanceof PickleJarBlockEntity be) {
                if(be.getFluid() == PickleJarFluid.WATER) {
                    be.emptyFluid(true);
                    return new ItemStack(Items.WATER_BUCKET);
                }
            }
            return null;
        });
        register(Items.WATER_BUCKET, (pointer, stack) -> {
            BlockPos pos = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING));
            ServerWorld world = pointer.world();
            if(world.getBlockEntity(pos) instanceof PickleJarBlockEntity be) {
                if(be.getFluid() == PickleJarFluid.AIR) {
                    be.fillWater(true);
                    return new ItemStack(Items.BUCKET);
                }
            }
            return null;
        });
        register(ItemsRegistry.SALT, (pointer, stack) -> {
            BlockPos pos = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING));
            ServerWorld world = pointer.world();
            if(world.getBlockEntity(pos) instanceof PickleJarBlockEntity be) {
                if(be.getFluid() == PickleJarFluid.WATER && be.getItemCount() > 0) {
                    be.startPickling();
                    stack.decrement(1);
                    be.update();
                    return stack;
                }
            }
            return null;
        });
    }
}
