package com.moigferdsrte.sandwichable.blocks;

import com.moigferdsrte.sandwichable.blocks.extra.BlockProperties;
import com.moigferdsrte.sandwichable.util.Util;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class ShrubBlock extends PlantBlock {

    public static final BooleanProperty SNIPPED;

    public ShrubBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(SNIPPED, false));
    }

    @Override
    protected MapCodec<? extends PlantBlock> getCodec() {
        return createCodec(ShrubBlock::new);
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView view, BlockPos pos) {
        Block block = floor.getBlock();
        return block == Blocks.GRASS_BLOCK || block == Blocks.DIRT || block == Blocks.COARSE_DIRT || block == Blocks.PODZOL || block == Blocks.FARMLAND || block == Blocks.SAND;
    }

    public static boolean canGenerateOn(BlockState state, WorldView world, BlockPos pos) {
        BlockPos blockPos = pos.down();
        Block block = world.getBlockState(blockPos).getBlock();
        return block == Blocks.GRASS_BLOCK || block == Blocks.DIRT || block == Blocks.COARSE_DIRT || block == Blocks.PODZOL;
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.scheduledTick(state, world, pos, random);
        if(world.getBlockState(pos.down()).getBlock().equals(Blocks.SAND)) {
            if (!state.get(SNIPPED)) {
                Util.scatterDroppedBlockDust(world, pos, this, 2, 30);
                world.setBlockState(pos, this.getDefaultState().with(SNIPPED, true));
            } else if (state.get(SNIPPED)) {
                world.setBlockState(pos, Blocks.DEAD_BUSH.getDefaultState());
            }
        }
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.fullCube();
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(player.preferredHand);
        if(stack.getItem() == Items.SHEARS && !state.get(SNIPPED)) {
            stack.damage(1, player, EquipmentSlot.MAINHAND);
            Util.scatterBlockDust(world, pos, this, 2, 30);
            world.setBlockState(pos, world.getBlockState(pos).with(SNIPPED, true));
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(SNIPPED);
    }

    static {
        SNIPPED = BlockProperties.SNIPPED;
    }
}
