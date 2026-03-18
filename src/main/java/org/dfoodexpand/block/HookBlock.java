package org.dfoodexpand.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.dfoodexpand.block.entity.HookBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class HookBlock extends BlockWithEntity {
    private static final Set<Item> TWO_BLOCK_SPACE_ITEMS = new HashSet<>();
    public static final DirectionProperty FACING = Properties.HOPPER_FACING;
    protected static final VoxelShape SHAPE = Block.createCuboidShape(6.5, 8.0, 6.5, 9.5, 16.0, 9.5);

    public HookBlock(Settings settings) {
        super(settings);
    }

    static {
        addAllowedItem(Items.BEEF);
        addAllowedItem(Items.COOKED_BEEF);
        addAllowedItem(Items.PORKCHOP);
        addAllowedItem(Items.COOKED_PORKCHOP);
        addAllowedItem(Items.MUTTON);
        addAllowedItem(Items.COOKED_MUTTON);

        addAllowedItem(Items.COD);
        addAllowedItem(Items.COOKED_COD);
        addAllowedItem(Items.SALMON);
        addAllowedItem(Items.COOKED_SALMON);
        addAllowedItem(Items.TROPICAL_FISH);
    }

    public static void addAllowedItem(Item item) {
        TWO_BLOCK_SPACE_ITEMS.add(item);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.up()).isOf(Blocks.CHAIN);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return !state.canPlaceAt(world, pos)
                ? Blocks.AIR.getDefaultState()
                : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof Inventory inventory) {
                ItemScatterer.spawn(world, pos, inventory);
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof HookBlockEntity hookEntity)) return ActionResult.PASS;

        ItemStack handStack = player.getStackInHand(hand);

        if (!hookEntity.isEmpty()) {
            // 钩子非空
            hookEntity.removeStack(0);
            // 从玩家手中移除一个物品
            if (!player.isCreative()) {
                handStack.decrement(1);
            }

            // 播放放置音效
            world.playSound(null, pos, SoundEvents.BLOCK_CHAIN_PLACE, SoundCategory.BLOCKS, 0.5f, 1.0f);
            return ActionResult.SUCCESS;
        } else {
            // 钩子为空
            if (!handStack.isEmpty() && TWO_BLOCK_SPACE_ITEMS.contains(handStack.getItem())) {
                ItemStack toPlace = handStack.copy();
                toPlace.setCount(1);
                hookEntity.setStack(0, toPlace);
                handStack.decrement(1);

                // 播放取下音效
                world.playSound(null, pos, SoundEvents.BLOCK_CHAIN_BREAK, SoundCategory.BLOCKS, 0.5f, 1.0f);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new HookBlockEntity(pos, state);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing());
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
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