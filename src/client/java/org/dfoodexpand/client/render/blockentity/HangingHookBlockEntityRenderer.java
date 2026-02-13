package org.dfoodexpand.client.render.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import org.dfood.render.HangingItemTransform;
import org.dfood.util.DFoodUtils;
import org.dfoodexpand.block.HangingHookBlock;
import org.dfoodexpand.block.entity.HangingHookBlockEntity;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

public class HangingHookBlockEntityRenderer implements BlockEntityRenderer<HangingHookBlockEntity> {
    public static final Map<Item, HangingItemTransform> ITEM_TRANSFORMS = new HashMap<>();

    protected final BlockRenderManager blockRenderer;

    public HangingHookBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.blockRenderer = context.getRenderManager();
    }

    /**
     * 应用物品的特殊变换
     * @param item 对应的物品堆栈
     * @param matrixStack 初始矩阵堆栈
     * @apiNote 如果没有对应的变化则不进行任何操作
     */
    public static void applyTransformation(ItemStack item, MatrixStack matrixStack) {
        if (ITEM_TRANSFORMS.containsKey(item.getItem())){
            ITEM_TRANSFORMS.get(item.getItem()).transformFunction().accept(matrixStack);
        }
    }

    static {
        ITEM_TRANSFORMS.put(Items.COD, new HangingItemTransform(matrixStack -> {
            matrixStack.translate(0.87f,  -0.64f, 0.14f);
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90f));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180f));
        }));
        ITEM_TRANSFORMS.put(Items.COOKED_COD, new HangingItemTransform(matrixStack -> {
            matrixStack.translate(0.87f,  -0.64f, 0.14f);
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90f));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180f));
        }));
        ITEM_TRANSFORMS.put(Items.SALMON, new HangingItemTransform(matrixStack -> {
            matrixStack.translate(0.93f,  -0.85f, 0.14f);
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90f));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180f));
        }));
        ITEM_TRANSFORMS.put(Items.COOKED_SALMON, new HangingItemTransform(matrixStack -> {
            matrixStack.translate(0.93f,  -0.85f, 0.14f);
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90f));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180f));
        }));
        ITEM_TRANSFORMS.put(Items.TROPICAL_FISH, new HangingItemTransform(matrixStack -> {
            matrixStack.translate(0.94f,  -0.6f, 0.14f);
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90f));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180f));
        }));
    }

    @Override
    public void render(HangingHookBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity == null || entity.getWorld() == null) {
            return;
        }

        BlockState state = entity.getWorld().getBlockState(entity.getPos());
        if (!(state.getBlock() instanceof HangingHookBlock)) {
            return;
        }

        Direction facing = state.get(HangingHookBlock.FACING);

        // 渲染四个槽位的物品
        for (int i = 0; i < 4; i++) {
            ItemStack stack = entity.getStack(i);
            if (!stack.isEmpty()) {
                renderSideItem(entity, stack, i, matrices, vertexConsumers, light, overlay, facing);
            }
        }
    }

    /**
     * 渲染挂在钩子上的物品。
     *
     * @param hangingHookBlockEntity 挂钩方块实体
     * @param index 要渲染的槽位（0-3）
     * @param stack 需要渲染的物品
     * @param facing 方块的朝向
     */
    @Unique
    private void renderSideItem(HangingHookBlockEntity hangingHookBlockEntity, ItemStack stack,
                                int index, MatrixStack matrixStack,
                                VertexConsumerProvider vertexConsumerProvider,
                                int light, int overlay, Direction facing) {

        matrixStack.push();

        // 根据朝向和槽位索引计算位置
        float xOffset;
        float yOffset;
        float zOffset;
        float rotationY;

        // 计算水平位置偏移（基于槽位索引）
        float horizontalOffset = (index * 0.25f) - 0.3f;

        switch (index) {
            case 0:
                horizontalOffset -= 0.05F;
                break;
            case 1:
                horizontalOffset -= 0.02F;
                break;
            case 2:
                horizontalOffset += 0.0F;
                break;
            case 3:
                horizontalOffset += 0.02F;
                break;
        }

        switch (facing) {
            case NORTH:
                // 朝北时，物品沿X轴排列
                xOffset = horizontalOffset;
                zOffset = 0.0f;
                rotationY = 0.0f;
                break;
            case SOUTH:
                // 朝南时，物品沿X轴排列（反向）
                xOffset = 1.0f - horizontalOffset;
                zOffset = 1.0f;
                rotationY = 180.0f;
                break;
            case EAST:
                // 朝东时，物品沿Z轴排列
                xOffset = 1.0f;
                zOffset = horizontalOffset;
                rotationY = 270.0f;
                break;
            case WEST:
                // 朝西时，物品沿Z轴排列（反向）
                xOffset = 0.0f;
                zOffset = 1.0f - horizontalOffset;
                rotationY = 90.0f;
                break;
            default:
                // 默认朝北
                xOffset = horizontalOffset;
                zOffset = 0.6f;
                rotationY = 0.0f;
        }

        // 垂直位置（悬挂高度）
        yOffset = 0.7f;

        // 应用变换
        matrixStack.translate(xOffset, yOffset, zOffset);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotationY));
        applyTransformation(stack, matrixStack);

        BlockState renderState = DFoodUtils.getBlockStateFromItem(stack.getItem());
        if (renderState != null) {
            blockRenderer.renderBlock(renderState, hangingHookBlockEntity.getPos(), hangingHookBlockEntity.getWorld(),
                    matrixStack, vertexConsumerProvider.getBuffer(RenderLayers.getBlockLayer(renderState)), true, Random.create());
        }

        matrixStack.pop();
    }

    @Override
    public boolean rendersOutsideBoundingBox(HangingHookBlockEntity blockEntity) {
        // 如果所有槽位都为空，则不渲染
        return !blockEntity.isEmpty();
    }
}