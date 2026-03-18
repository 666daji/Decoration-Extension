package org.dfoodexpand.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import org.dfoodexpand.client.render.entity.ChairEntityRenderer;
import org.dfoodexpand.client.render.model.ModModelLoader;
import org.dfoodexpand.client.render.model.ModRenderLayers;
import org.dfoodexpand.registry.ModEntities;

public class DFoodExpandClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModelLoadingPlugin.register(new ModModelLoader());
        ModRenderLayers.registryRenderLayer();
        EntityRendererRegistry.register(ModEntities.CHAIR, ChairEntityRenderer::new);
    }
}
