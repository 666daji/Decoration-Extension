package org.dfoodexpand.client.render.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import org.dfoodexpand.block.PicnicBasketBlock;
import org.dfoodexpand.block.entity.PicnicBasketBlockEntity;
import org.dfoodexpand.client.render.model.ModModelLoader;

public class PicnicBasketBlockEntityRenderer implements BlockEntityRenderer<PicnicBasketBlockEntity> {
    private final BakedModelManager modelManager;
    private final BlockModelRenderer modelRenderer;

    public PicnicBasketBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.modelManager = context.getRenderManager().getModels().getModelManager();
        this.modelRenderer = context.getRenderManager().getModelRenderer();
    }

    @Override
    public void render(PicnicBasketBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BlockState state = entity.getCachedState();
        if (state.get(PicnicBasketBlock.IS_OPEN) && !entity.isEmpty()) {
            matrices.push();
            matrices.translate(0.5, 0, 0.5);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(state.get(PicnicBasketBlock.FACING).asRotation() + 90));
            matrices.translate(-0.5, 0, -0.5);

            ItemStack stack = entity.getStack(0);
            BakedModel model = modelManager.getModel(ModModelLoader.createBasketPlaceModel(stack.getItem(), stack.getCount()));

            if (model != null && model != modelManager.getMissingModel()) {
                modelRenderer.render(
                        entity.getWorld(),
                        model,
                        entity.getCachedState(),
                        entity.getPos(),
                        matrices,
                        vertexConsumers.getBuffer(RenderLayer.getCutout()),
                        true,
                        Random.create(),
                        entity.getCachedState().getRenderingSeed(entity.getPos()),
                        OverlayTexture.DEFAULT_UV
                );
            }

            matrices.pop();
        }
    }
}
