package org.dfoodexpand.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.dfoodexpand.registry.ModEntities;

public class ChairEntity extends Entity {
    public ChairEntity(EntityType<? extends ChairEntity> type, World world) {
        super(type, world);
        this.noClip = true;
        this.setInvulnerable(true);
        this.setInvisible(true);
    }

    public ChairEntity(World world, BlockPos pos) {
        this(ModEntities.CHAIR, world);
        setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5); // 方块中心偏上
    }

    @Override
    protected void initDataTracker() {}

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {}

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {}

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        if (player.shouldCancelInteraction()) return ActionResult.PASS;
        if (this.hasPassengers()) return ActionResult.PASS;
        if (!this.getWorld().isClient) {
            player.startRiding(this);
            return ActionResult.CONSUME;
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void tick() {
        super.tick();
        // 如果椅子没有乘客，自动移除（例如玩家下车后）
        if (!this.getWorld().isClient && !this.hasPassengers()) {
            this.discard();
        }
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        // 乘客下车后，如果还有乘客就不移除，否则在 tick 中移除
        if (!this.getWorld().isClient && !this.hasPassengers()) {
            this.discard();
        }
    }

    @Override
    public double getMountedHeightOffset() {
        return -0.3;
    }

    @Override
    public Vec3d updatePassengerForDismount(LivingEntity passenger) {
        // 下车时让乘客回到椅子方块的位置
        BlockPos pos = this.getBlockPos();
        return new Vec3d(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5);
    }
}