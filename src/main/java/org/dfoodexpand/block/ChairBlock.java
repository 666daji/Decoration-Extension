package org.dfoodexpand.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.dfoodexpand.entity.ChairEntity;

public class ChairBlock extends Block {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    public ChairBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(FACING, net.minecraft.util.math.Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) return ActionResult.SUCCESS;

        // 检查是否已经有椅子实体存在
        if (!world.getEntitiesByClass(ChairEntity.class, new net.minecraft.util.math.Box(pos), e -> true).isEmpty()) {
            return ActionResult.PASS; // 已经有椅子了
        }

        // 创建椅子实体
        ChairEntity chair = new ChairEntity(world, pos);
        world.spawnEntity(chair);
        player.startRiding(chair);

        return ActionResult.CONSUME;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            // 方块被破坏时，移除所有关联的椅子实体
            world.getEntitiesByClass(ChairEntity.class, new net.minecraft.util.math.Box(pos), e -> true)
                    .forEach(Entity::discard);
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }
}