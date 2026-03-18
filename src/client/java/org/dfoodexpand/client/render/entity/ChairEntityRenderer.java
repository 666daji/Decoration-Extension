package org.dfoodexpand.client.render.entity;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import org.dfoodexpand.entity.ChairEntity;

public class ChairEntityRenderer extends EntityRenderer<ChairEntity> {
    public ChairEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(ChairEntity entity) {
        return null;
    }
}
