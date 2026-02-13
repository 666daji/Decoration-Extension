package org.dfoodexpand.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.dfoodexpand.registry.ModBlockEntityTypes;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class PicnicBasketBlockEntity extends BlockEntity implements Inventory {
    public static final Map<Item, Integer> CAN_PLACE_COUNT = new HashMap<>();

    protected final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);

    public PicnicBasketBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.PICNIC_BASKET, pos, state);
    }

    public static int getCanPlaceCount(Item item) {
        if (CAN_PLACE_COUNT.containsKey(item)) {
            return CAN_PLACE_COUNT.get(item);
        }

        return 0;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return inventory.get(0).isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        if (slot == 0) return inventory.get(0);
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack result = Inventories.splitStack(this.inventory, slot, amount);
        if (!result.isEmpty()) {
            markDirty();
        }
        return result;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack result = Inventories.removeStack(this.inventory, slot);
        if (!result.isEmpty()) {
            markDirty();
        }
        return result;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if (slot == 0) {
            inventory.set(0, stack);
            markDirty();
        }
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        BlockPos pos = this.pos;
        return player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0; // 距离 ≤ 8
    }

    @Override
    public void clear() {
        inventory.set(0, ItemStack.EMPTY);
        markDirty();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        inventory.clear();
        Inventories.readNbt(nbt, inventory);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
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

    static {
        CAN_PLACE_COUNT.put(Items.SALMON, 6);
        CAN_PLACE_COUNT.put(Items.COOKED_SALMON, 6);
        CAN_PLACE_COUNT.put(Items.GLOW_BERRIES, 6);
        CAN_PLACE_COUNT.put(Items.CARROT, 6);
        CAN_PLACE_COUNT.put(Items.GOLDEN_CARROT, 6);
        CAN_PLACE_COUNT.put(Items.SWEET_BERRIES, 6);
        CAN_PLACE_COUNT.put(Items.BREAD, 4);
        CAN_PLACE_COUNT.put(Items.APPLE, 6);
        CAN_PLACE_COUNT.put(Items.GOLDEN_APPLE, 6);
        CAN_PLACE_COUNT.put(Items.BEETROOT, 6);
        CAN_PLACE_COUNT.put(Items.POTATO, 6);
        CAN_PLACE_COUNT.put(Items.BAKED_POTATO, 6);
        CAN_PLACE_COUNT.put(Items.COD, 6);
        CAN_PLACE_COUNT.put(Items.COOKED_COD, 6);
        CAN_PLACE_COUNT.put(Items.CHORUS_FRUIT, 6);
    }
}
