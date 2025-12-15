package com.moigferdsrte.sandwichable.blocks;

import com.moigferdsrte.sandwichable.blocks.entity.SandwichBlockEntity;
import com.moigferdsrte.sandwichable.blocks.extra.ModelBlockWithEntity;
import com.moigferdsrte.sandwichable.registry.BlocksRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.component.DataComponentTypes.BLOCK_ENTITY_DATA;

public class SandwichBlock extends ModelBlockWithEntity {

    public SandwichBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return createCodec(SandwichBlock::new);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return !world.getBlockState(pos.down()).getBlock().equals(BlocksRegistry.SANDWICH_TABLE);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        super.neighborUpdate(state, world, pos, block, fromPos, notify);
        if(!canPlaceAt(state, world, pos)) {
            world.breakBlock(pos, true);
            dropItem(world, pos);
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        if (view.getBlockEntity(pos) instanceof SandwichBlockEntity blockEntity) {
            double size;
            size = (blockEntity.getSandwich().getSize() * 0.5D);
            return Block.createCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, size, 12.0D);
        }
        return Block.createCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 12.0D);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SandwichBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if(!player.isCreative()) dropItem(world, pos);
        super.onBreak(world, pos, state, player);
        return state;
    }

    public void dropItem(World world, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof SandwichBlockEntity blockEntity) {
            blockEntity.getSandwich().ejectSandwich(world, new Vec3d(pos.getX()+0.5, pos.getY() - 0.7, pos.getZ() + 0.5));
        }
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        if (world.getBlockEntity(pos) instanceof SandwichBlockEntity blockEntity) {
            ItemStack item = new ItemStack(BlocksRegistry.SANDWICH);
            NbtCompound tag = blockEntity.getSandwich().writeToNbt(new NbtCompound());
            if(!tag.isEmpty()) {
                item.set(BLOCK_ENTITY_DATA, NbtComponent.of(tag));
            }
            return item;
        }
        return ItemStack.EMPTY;
    }
}
