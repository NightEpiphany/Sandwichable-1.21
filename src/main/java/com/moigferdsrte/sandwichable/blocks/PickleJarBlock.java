package com.moigferdsrte.sandwichable.blocks;

import com.moigferdsrte.sandwichable.blocks.entity.PickleJarBlockEntity;
import com.moigferdsrte.sandwichable.blocks.extra.ModelBlockWithEntity;
import com.moigferdsrte.sandwichable.items.PickleJarBlockItem;
import com.moigferdsrte.sandwichable.registry.BlocksRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class PickleJarBlock extends ModelBlockWithEntity {
    public static final VoxelShape SHAPE;

    public PickleJarBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return createCodec(PickleJarBlock::new);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if(world.getBlockEntity(pos) instanceof PickleJarBlockEntity) {
            return ((PickleJarBlockEntity)world.getBlockEntity(pos)).onUse(world, player, player.preferredHand, pos);
        }
        return ActionResult.FAIL;
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        if(world.getBlockEntity(pos) instanceof PickleJarBlockEntity) {
            return PickleJarBlockItem.createFromBlockEntity((PickleJarBlockEntity)world.getBlockEntity(pos));
        }
        return super.getPickStack(world, pos, state);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (world.getBlockEntity(pos) instanceof PickleJarBlockEntity && !player.isCreative()) {
            ItemEntity itemEntity = new ItemEntity(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, PickleJarBlockItem.createFromBlockEntity((PickleJarBlockEntity)world.getBlockEntity(pos)));
            itemEntity.setToDefaultPickupDelay();
            world.spawnEntity(itemEntity);
        }
        super.onBreak(world, pos, state, player);
        return state;
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        if(world.getBlockEntity(pos) instanceof PickleJarBlockEntity) {
            return ((PickleJarBlockEntity)world.getBlockEntity(pos)).areItemsPickled() ? 15 : 0;
        }
        return super.getComparatorOutput(state, world, pos);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PickleJarBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, BlocksRegistry.PICKLEJAR_BLOCKENTITY, PickleJarBlockEntity::tick);
    }

    static {
        SHAPE = VoxelShapes.union(
            Block.createCuboidShape(2, 0, 2, 14, 13, 14),
            Block.createCuboidShape(3, 13, 3, 13, 16, 13)
        );
    }
}
