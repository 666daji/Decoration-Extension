package org.dfoodexpand.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import org.dfoodexpand.client.render.model.ModModelLoader;
import org.dfoodexpand.client.render.model.ModRenderLayers;

public class DFoodExpandClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModelLoadingPlugin.register(new ModModelLoader());
        ModRenderLayers.registryRenderLayer();
    }
}
