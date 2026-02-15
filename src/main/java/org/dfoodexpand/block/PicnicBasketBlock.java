package org.dfoodexpand.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.dfood.util.DFoodUtils;
import org.dfoodexpand.block.entity.PicnicBasketBlockEntity;
import org.dfoodexpand.registry.ModBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PicnicBasketBlock extends BlockWithEntity implements HaveClickInteractBlock {
    private static final Map<DyedCarpetBlock, PicnicBasketBlock> ALL = new HashMap<>();
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty IS_OPEN = BooleanProperty.of("is_open");
    public static final VoxelShape Y_SHAPE = Block.createCuboidShape(2, 0, 0, 14, 7, 16);
    public static final VoxelShape X_SHAPE = Block.createCuboidShape(0, 0, 2, 16, 7, 14);

    /**
     * 当前野餐篮拥有的染色地毯，为null时表示没有染色地毯。
     */
    public final @Nullable DyedCarpetBlock dyedCarpet;

    public PicnicBasketBlock(Settings settings, @Nullable DyedCarpetBlock dyedCarpet) {
        super(settings);
        this.dyedCarpet = dyedCarpet;
        setDefaultState(getDefaultState().with(IS_OPEN, false));
        ALL.put(dyedCarpet, this);
    }

    public static PicnicBasketBlock[] getAll() {
        return PicnicBasketBlock.ALL.values().toArray(new PicnicBasketBlock[]{});
    }

    public static BlockState getTargetState(DyedCarpetBlock dyedCarpetBlock) {
        return ALL.get(dyedCarpetBlock).getDefaultState();
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction facing = state.get(FACING);
        if (facing.getAxis() == Direction.Axis.X) {
            return X_SHAPE;
        }

        return Y_SHAPE;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity entity = world.getBlockEntity(pos);
        ItemStack stack = player.getStackInHand(hand);
        boolean current = state.get(IS_OPEN);

        if (!(entity instanceof PicnicBasketBlockEntity blockEntity)) {
            return ActionResult.PASS;
        }

        // 尝试裹染色地毯
        BlockState stackBlock = DFoodUtils.getBlockStateFromItem(stack.getItem());
        if (stackBlock != null && stackBlock.getBlock() instanceof DyedCarpetBlock dyedCarpetBlock && current && dyedCarpet == null) {
            // 构建新的方块数据
            BlockState newState = getTargetState(dyedCarpetBlock)
                    .with(FACING, state.get(FACING))
                    .with(IS_OPEN, true);
            NbtCompound nbt = blockEntity.createNbt();

            // 替换为新的野餐篮
            world.setBlockState(pos, newState);
            world.playSound(null, pos, SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.BLOCKS, 0.5f, 1.0f);
            Objects.requireNonNull(world.getBlockEntity(pos)).readNbt(nbt);

            // 减少物品数量
            if (!player.isCreative()) {
                ItemStack newStack =  stack.copy();
                newStack.decrement(1);
                player.setStackInHand(hand, newStack);
            }

            return ActionResult.SUCCESS;
        }

        ItemStack basketStack = blockEntity.getStack(0);
        int maxCount = PicnicBasketBlockEntity.getCanPlaceCount(stack.getItem());

        // 放入物品
        boolean canInsert = maxCount > 0 &&
                (basketStack.isEmpty() ||
                        (ItemStack.areItemsEqual(basketStack, stack) && basketStack.getCount() < maxCount));
        if (canInsert && current) {
            if (basketStack.isEmpty()) {
                blockEntity.setStack(0, stack.copyWithCount(1));
            } else {
                basketStack.increment(1);
            }
            if (!player.isCreative()) {
                stack.decrement(1);
            }
            world.playSound(null, pos, getSound(stack), SoundCategory.BLOCKS, 0.3f, 1.0f);
            blockEntity.markDirty();
            return ActionResult.SUCCESS;
        }

        // 取出物品
        if (!basketStack.isEmpty() && current) {
            ItemStack extracted = blockEntity.removeStack(0, 1);
            if (!extracted.isEmpty()) {
                player.getInventory().offerOrDrop(extracted);
                world.playSound(null, pos, getSound(stack), SoundCategory.BLOCKS, 0.3f, 0.8f);
                blockEntity.markDirty();
                return ActionResult.SUCCESS;
            }
        }

        // 尝试取出物品
        return ActionResult.PASS;
    }

    @Override
    public void onClickBlock(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, Direction direction) {
        BlockEntity entity = world.getBlockEntity(pos);
        boolean current = state.get(IS_OPEN);

        // 轻击时交互盖子
        if (!(entity instanceof PicnicBasketBlockEntity blockEntity)) {
            return;
        }

        if (player.isSneaking() && state.getBlock() instanceof PicnicBasketBlock block && block.dyedCarpet != null && current) {
            // 下蹲时尝试取下染色地毯
            BlockState newState = ModBlocks.PICNIC_BASKET.getDefaultState()
                    .with(FACING, state.get(FACING))
                    .with(IS_OPEN, true);
            NbtCompound nbt = blockEntity.createNbt();

            // 替换为新的野餐篮
            world.setBlockState(pos, newState);
            world.playSound(null, pos, SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.BLOCKS, 0.5f, 1.0f);
            Objects.requireNonNull(world.getBlockEntity(pos)).readNbt(nbt);

            ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(block.dyedCarpet.asItem()));
        } else {
            // 操作盖子
            world.setBlockState(pos, state.with(IS_OPEN, !current));
            world.playSound(null, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 0.5f, 1.0f);
        }
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        BlockEntity entity = builder.get(LootContextParameters.BLOCK_ENTITY);
        ItemStack dropStack = new ItemStack(asItem());

        if (entity instanceof PicnicBasketBlockEntity blockEntity) {
            ItemStack content = blockEntity.getStack(0);

            if (!content.isEmpty()) {
                Optional<NbtElement> optional = ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, content).result();
                if (optional.isPresent()) {
                    NbtElement contentNbt = optional.get();
                    NbtCompound nbt = dropStack.getOrCreateNbt();
                    nbt.put("Item", contentNbt);
                }
            }
        }

        return List.of(dropStack);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof PicnicBasketBlockEntity blockEntity) {
            NbtCompound nbt = itemStack.getNbt();

            if (nbt != null && nbt.contains("Item")) {
                NbtElement contentNbt = nbt.get("Item");

                // 反序列化为 ItemStack
                Optional<ItemStack> optional = ItemStack.CODEC.parse(NbtOps.INSTANCE, contentNbt).result();
                optional.ifPresent(content -> blockEntity.setStack(0, content));
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        NbtCompound blockEntityTag = stack.getNbt();

        if (blockEntityTag != null && blockEntityTag.contains("Item")) {
            NbtElement contentNbt = blockEntityTag.get("Item");
            Optional<ItemStack> optional = ItemStack.CODEC.parse(NbtOps.INSTANCE, contentNbt).result();

            if (optional.isPresent()) {
                ItemStack content = optional.get();
                Text itemName = content.getName();
                int count = content.getCount();

                if (itemName instanceof MutableText item) {
                    MutableText line = item.append("x" + count);
                    line.formatted(Formatting.ITALIC, Formatting.DARK_GRAY);
                    tooltip.add(line);
                }
            }
        }
    }

    /**
     * 获取物品的放置音效。
     *
     * @param stack 要获取的物品堆栈
     * @return 对应的放置音效
     */
    protected SoundEvent getSound(ItemStack stack) {
        BlockState state = DFoodUtils.getBlockStateFromItem(stack.getItem());
        if (state != null) {
            return state.getBlock().getSoundGroup(state).getPlaceSound();
        }

        return SoundEvents.BLOCK_WOOD_PLACE;
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing());
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PicnicBasketBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, IS_OPEN);
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
