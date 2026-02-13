package org.dfoodexpand.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.dfood.block.FoodBlock;
import org.dfood.util.DFoodUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;

@Mixin(ChickenEntity.class)
public class ChickenEntityMixin {

    /**
     * 更改鸡下蛋的逻辑，首先尝试放置对应的方块而不是直接掉落对应物品。
     *
     * @param chicken 原本的鸡实体
     * @param item 原本要掉落的物品
     * @return 如果成功掉落物品返回对应的物品堆栈实体，如果成功放置则返回null
     */
    @Redirect(
            method = "tickMovement()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/passive/ChickenEntity;dropItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;"))
    private ItemEntity redirectDropEgg(ChickenEntity chicken, ItemConvertible item) {
        World world = chicken.getWorld();

        // 获取鸡蛋对应的方块状态
        BlockState eggBlockState = DFoodUtils.getBlockStateFromItem(Items.EGG);
        if (eggBlockState == null || eggBlockState.isAir() || !(eggBlockState.getBlock() instanceof FoodBlock eggBlock)) {
            return chicken.dropItem(Items.EGG);
        }

        BlockPos chickenPos = chicken.getBlockPos();

        // 优先尝试堆叠到附近的鸡蛋方块（2格范围）
        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 2; y++) {
                for (int z = -2; z <= 2; z++) {
                    BlockPos checkPos = chickenPos.add(x, y, z);
                    BlockState state = world.getBlockState(checkPos);

                    // 检查是否为鸡蛋方块
                    if (state.getBlock() == eggBlock) {
                        int currentCount = state.get(eggBlock.NUMBER_OF_FOOD);
                        int maxCount = eggBlock.MAX_FOOD;

                        // 检查是否已达到最大堆叠数
                        if (currentCount < maxCount) {
                            // 增加堆叠数量
                            BlockState newState = state.with(eggBlock.NUMBER_OF_FOOD, currentCount + 1);
                            if (world.setBlockState(checkPos, newState, 3)) {
                                return null; // 堆叠成功
                            }
                        }
                    }
                }
            }
        }

        // 尝试在附近位置放置鸡蛋方块
        List<BlockPos> candidatePositions = new ArrayList<>();
        eggBlockState.with(FoodBlock.FACING, chicken.getHorizontalFacing());
        BlockPos[] offsets = {
                new BlockPos(0, 0, 0),    // 当前位置
                new BlockPos(0, 0, 1),    // 北
                new BlockPos(0, 0, -1),   // 南
                new BlockPos(1, 0, 0),    // 东
                new BlockPos(-1, 0, 0),   // 西
                new BlockPos(1, 0, 1),    // 东北
                new BlockPos(-1, 0, 1),   // 西北
                new BlockPos(1, 0, -1),   // 东南
                new BlockPos(-1, 0, -1)   // 西南
        };

        for (BlockPos offset : offsets) {
            candidatePositions.add(chickenPos.add(offset));
        }

        for (BlockPos pos : candidatePositions) {
            // 检查位置是否可放置（有支撑方块）
            if (!world.getBlockState(pos.down()).isSolidBlock(world, pos.down())) {
                continue;
            }

            BlockState existingState = world.getBlockState(pos);

            // 构建 ItemPlacementContext 用于 canReplace 检查
            ItemPlacementContext context = new ItemPlacementContext(
                    world,
                    null, // 没有玩家
                    null, // 没有手
                    Items.EGG.getDefaultStack(),
                    new BlockHitResult(
                            Vec3d.ofCenter(pos),
                            Direction.UP,
                            pos,
                            false
                    )
            );

            // 检查鸡蛋方块是否可以放置在这个位置
            if (!eggBlockState.canPlaceAt(world, pos)) {
                continue;
            }

            // 如果是空气，直接放置
            if (existingState.isAir()) {
                if (world.setBlockState(pos, eggBlockState, 3)) {
                    return null; // 放置成功
                }
            }
            // 如果是可替换方块，先破坏再放置
            else if (existingState.canReplace(context)) {
                // 破坏原方块
                world.breakBlock(pos, true, chicken);
                // 放置鸡蛋方块
                if (world.setBlockState(pos, eggBlockState, 3)) {
                    return null; // 放置成功
                }
            }
            // 对于其他不可替换方块，跳过
        }

        // 所有尝试都失败，回退到原版逻辑
        return chicken.dropItem(item);
    }
}