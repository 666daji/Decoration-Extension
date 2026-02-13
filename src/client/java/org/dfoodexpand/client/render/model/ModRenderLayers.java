package org.dfoodexpand.client.render.model;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import org.dfoodexpand.block.PicnicBasketBlock;

public class ModRenderLayers {
    private static final BlockRenderLayerMap INSTANCE = BlockRenderLayerMap.INSTANCE;

    public static void registryRenderLayer() {
        INSTANCE.putBlocks(RenderLayer.getCutout(), PicnicBasketBlock.getAll());
    }
}
