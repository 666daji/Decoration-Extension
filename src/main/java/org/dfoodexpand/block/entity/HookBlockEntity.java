package org.dfoodexpand.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.dfoodexpand.registry.ModBlockEntityTypes;
import org.jetbrains.annotations.Nullable;

public class HookBlockEntity extends BlockEntity implements Inventory {
    private ItemStack stack = ItemStack.EMPTY;

    public HookBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.HOOK, pos, state);
    }

    @Override
    public int size() {
        return 1; // 只有一个槽位
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return slot == 0 ? stack : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        if (slot == 0 && !stack.isEmpty()) {
            ItemStack removed = stack.split(amount);
            if (stack.isEmpty()) markDirty();
            return removed;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot) {
        if (slot == 0) {
            ItemStack old = stack;
            stack = ItemStack.EMPTY;
            markDirty();
            return old;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if (slot == 0) {
            this.stack = stack;
            if (stack.getCount() > 1) this.stack.setCount(1); // 强制只存一个
            markDirty();
        }
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0;
    }

    @Override
    public void clear() {
        stack = ItemStack.EMPTY;
        markDirty();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        stack = ItemStack.EMPTY;
        stack = nbt.contains("Item") ? ItemStack.fromNbt(nbt.getCompound("Item")) : ItemStack.EMPTY;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (!stack.isEmpty()) {
            nbt.put("Item", stack.writeNbt(new NbtCompound()));
        }
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (world != null) {
            world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), 3);
        }
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
}