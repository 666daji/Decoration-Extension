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
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.dfoodexpand.block.entity.HangingHookBlockEntity;
import org.dfoodexpand.registry.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class HangingHookBlock extends BlockWithEntity {
    private static final Set<Item> TWO_BLOCK_SPACE_ITEMS = new HashSet<>();
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    private static final VoxelShape WEST_SHAPE = Block.createCuboidShape(0,10,0,2,16,16);
    private static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0,10,0,16,16,2);
    private static final VoxelShape EAST_SHAPE = Block.createCuboidShape(14,10,0,16,16,16);
    private static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0,10,14,16,16,16);

    public HangingHookBlock(Settings settings) {
        super(settings);
    }

    static {
        TWO_BLOCK_SPACE_ITEMS.add(Items.SALMON);
        TWO_BLOCK_SPACE_ITEMS.add(Items.COOKED_SALMON);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);
        return switch (direction) {
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            case EAST -> EAST_SHAPE;
            default -> throw new IllegalStateException("Unexpected direction: " + direction);
        };
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        Vec3d relative = hit.getPos().subtract(Vec3d.of(pos));
        int hitIndex = computeIndex(state.get(FACING), relative);

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof HangingHookBlockEntity hookEntity)) {
            return ActionResult.PASS;
        }

        ItemStack handStack = player.getStackInHand(hand);
        ItemStack hookStack = hookEntity.getStack(hitIndex);

        if (hookStack.isEmpty()) {
            // 挂钩为空，尝试悬挂物品
            if (!handStack.isEmpty() && handStack.isIn(ModTags.HANGABLE_ITEMS)) {
                // 检查是否需要两格空间
                if (needsTwoBlockSpace(handStack)) {
                    // 检查下方是否为空气
                    BlockPos belowPos = pos.down();
                    if (!world.getBlockState(belowPos).isAir()) {
                        // 下方不是空气，不能悬挂
                        return ActionResult.FAIL;
                    }
                }

                // 悬挂一个物品到挂钩上
                ItemStack stackToPlace = handStack.copy();
                stackToPlace.setCount(1);
                hookEntity.setStack(hitIndex, stackToPlace);

                // 从玩家手中移除一个物品
                if (!player.isCreative()) {
                    handStack.decrement(1);
                }

                // 播放放置音效
                world.playSound(null, pos, SoundEvents.BLOCK_CHAIN_PLACE, SoundCategory.BLOCKS, 0.5f, 1.0f);

                // 标记方块实体已更新
                hookEntity.markDirty();

                return ActionResult.SUCCESS;
            }
        } else {
            // 挂钩有物品，取下物品给玩家
            player.giveItemStack(hookStack.copy());
            hookEntity.setStack(hitIndex, ItemStack.EMPTY);

            // 播放取下音效
            world.playSound(null, pos, SoundEvents.BLOCK_CHAIN_BREAK, SoundCategory.BLOCKS, 0.5f, 1.0f);

            hookEntity.markDirty();
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    /**
     * 计算在挂钩方块上的点击位置对应的分区索引。
     *
     * @param facing 方块的朝向，确定使用哪个坐标轴和映射方向
     * @param relative 点击位置相对于方块原点的坐标，各分量在 [0.0, 1.0] 范围内
     * @return 分区索引值，范围 0-3
     * @throws IllegalArgumentException 如果 relative 的坐标分量不在有效范围内（方法内部会进行裁剪）
     */
    private static int computeIndex(Direction facing, Vec3d relative) {
        // 根据朝向选择使用哪个坐标分量
        double value = switch (facing) {
            case EAST, WEST ->
                // EAST 和 WEST 时关心 Z 值
                    relative.getZ();
            default ->
                // NORTH 和 SOUTH 时关心 X 值
                    relative.getX();
        };

        // 根据朝向调整值的映射关系
        if (facing == Direction.WEST || facing == Direction.SOUTH) {
            // WEST 和 SOUTH 时，值越大索引越小
            value = 1.0 - value;
        }

        // 确保值在 [0.0, 1.0] 范围内
        value = Math.max(0.0, Math.min(1.0, value));

        // 将 [0,1] 分为 4 个区间，计算索引
        int index = (int) (value * 4);

        // 处理边界情况：当 value = 1.0 时
        if (index == 4) {
            index = 3;
        }

        return index;
    }

    /**
     * 判断物品是否需要两格空间
     */
    public static boolean needsTwoBlockSpace(ItemStack stack) {
        return TWO_BLOCK_SPACE_ITEMS.contains(stack.getItem());
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos checkPos = pos.offset(state.get(FACING));
        return !world.getBlockState(checkPos).isIn(org.dfood.tag.ModTags.FOOD_PLACE);
    }

    @Override
    public BlockState getStateForNeighborUpdate(
            BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos
    ) {
        // 检查下方方块变化
        if (direction == Direction.DOWN) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof HangingHookBlockEntity hookEntity) {
                // 检查是否有需要两格空间的物品
                for (int i = 0; i < hookEntity.size(); i++) {
                    ItemStack stack = hookEntity.getStack(i);
                    if (!stack.isEmpty() && needsTwoBlockSpace(stack)) {
                        // 下方不再是空气，物品掉落
                        if (!neighborState.isAir()) {
                            // 掉落物品
                            ItemScatterer.spawn((World) world, pos.getX(), pos.getY(), pos.getZ(), stack);

                            // 清空槽位
                            hookEntity.setStack(i, ItemStack.EMPTY);

                            // 播放掉落音效
                            world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.5f, 1.0f);
                        }
                    }
                }
                hookEntity.markDirty();
            }
        }

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
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new HangingHookBlockEntity(pos, state);
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
