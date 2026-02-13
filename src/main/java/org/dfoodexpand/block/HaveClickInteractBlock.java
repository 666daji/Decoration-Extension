package org.dfoodexpand.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public interface HaveClickInteractBlock {
    /**
     * 当玩家左键轻击方块时调用(破坏时间<=5tick)。
     * <p>此方法只会在服务端调用。</p>
     *
     * @param state     方块状态
     * @param world     世界
     * @param pos       方块坐标
     * @param player    点击方块的玩家
     * @param hand      使用的手(破坏时为主手)
     * @param direction 破坏方向
     */
    void onClickBlock(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, Direction direction);
}
