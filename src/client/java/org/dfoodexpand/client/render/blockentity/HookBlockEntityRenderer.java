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
import org.dfood.util.DFoodUtils;
import org.dfoodexpand.block.HookBlock;
import org.dfoodexpand.block.entity.HookBlockEntity;
import org.dfoodexpand.client.render.RenderUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class HookBlockEntityRenderer implements BlockEntityRenderer<HookBlockEntity> {
    public static final Map<Item, Consumer<MatrixStack>> ITEM_TRANSFORMS = new HashMap<>();

    protected final BlockRenderManager blockRenderer;

    public HookBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.blockRenderer = context.getRenderManager();
    }

    public static void applyTransformation(ItemStack itemStack, MatrixStack matrixStack) {
        Consumer<MatrixStack> transform = ITEM_TRANSFORMS.get(itemStack.getItem());
        if (transform != null) {
            transform.accept(matrixStack);
        }
    }

    static {
        ITEM_TRANSFORMS.put(Items.COD, matrixStack -> {
            matrixStack.translate(0.45f, -0.8f, 0.14f);
            RenderUtil.BASE_MUL.accept(matrixStack);
        });
        ITEM_TRANSFORMS.put(Items.COOKED_COD, matrixStack -> {
            matrixStack.translate(0.45f, -0.8f, 0.14f);
            RenderUtil.BASE_MUL.accept(matrixStack);
        });
        ITEM_TRANSFORMS.put(Items.SALMON, matrixStack -> {
            matrixStack.translate(0.5f, -0.85f, 0.1f);
            RenderUtil.BASE_MUL.accept(matrixStack);
        });
        ITEM_TRANSFORMS.put(Items.COOKED_SALMON, matrixStack -> {
            matrixStack.translate(0.5f, -0.85f, 0.1f);
            RenderUtil.BASE_MUL.accept(matrixStack);
        });
        ITEM_TRANSFORMS.put(Items.TROPICAL_FISH, matrixStack -> {
            matrixStack.translate(0.5f, -0.8f, 0.14f);
            RenderUtil.BASE_MUL.accept(matrixStack);
        });

        ITEM_TRANSFORMS.put(Items.BEEF, matrixStack -> {
            matrixStack.translate(0.6f, -0.85f, 0.14f);
            RenderUtil.BASE_MUL.accept(matrixStack);
        });
        ITEM_TRANSFORMS.put(Items.COOKED_BEEF, matrixStack -> {
            matrixStack.translate(0.6f, -0.85f, 0.14f);
            RenderUtil.BASE_MUL.accept(matrixStack);
        });
        ITEM_TRANSFORMS.put(Items.PORKCHOP, matrixStack -> {
            matrixStack.translate(0.6f, -0.85f, 0.14f);
            RenderUtil.BASE_MUL.accept(matrixStack);
        });
        ITEM_TRANSFORMS.put(Items.COOKED_PORKCHOP, matrixStack -> {
            matrixStack.translate(0.6f, -0.85f, 0.14f);
            RenderUtil.BASE_MUL.accept(matrixStack);
        });
        ITEM_TRANSFORMS.put(Items.MUTTON, matrixStack -> {
            matrixStack.translate(-0.55f, -0.85f, 0.14f);
            RenderUtil.BASE_MUL.accept(matrixStack);
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(270f));
        });
        ITEM_TRANSFORMS.put(Items.COOKED_MUTTON, matrixStack -> {
            matrixStack.translate(-0.55f, -0.85f, 0.14f);
            RenderUtil.BASE_MUL.accept(matrixStack);
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(270f));
        });
    }

    @Override
    public void render(HookBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity == null || entity.getWorld() == null) return;

        BlockState state = entity.getWorld().getBlockState(entity.getPos());
        if (!(state.getBlock() instanceof HookBlock)) return;

        ItemStack stack = entity.getStack(0);
        if (stack.isEmpty()) return;

        Direction facing = state.get(HookBlock.FACING);

        matrices.push();

        // 将物品放在方块中心偏下
        float xOffset = 0.5f;
        float yOffset = 0.7f;
        float zOffset = 0.5f;

        matrices.translate(xOffset, yOffset, zOffset);

        // 根据朝向旋转物品
        float rotationY = switch (facing) {
            case NORTH -> 0.0f;
            case SOUTH -> 180.0f;
            case EAST -> 270.0f;
            case WEST -> 90.0f;
            default -> 0.0f;
        };
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotationY));

        // 应用物品特定变换
        applyTransformation(stack, matrices);

        // 将物品渲染为对应方块
        BlockState renderState = DFoodUtils.getBlockStateFromItem(stack.getItem());
        if (renderState != null) {
            blockRenderer.renderBlock(renderState, entity.getPos(), entity.getWorld(),
                    matrices, vertexConsumers.getBuffer(RenderLayers.getBlockLayer(renderState)),
                    true, Random.create());
        }

        matrices.pop();
    }

    @Override
    public boolean rendersOutsideBoundingBox(HookBlockEntity blockEntity) {
        return !blockEntity.isEmpty();
    }
}