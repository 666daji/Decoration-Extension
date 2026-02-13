package org.dfoodexpand.client.mixin;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import org.dfoodexpand.client.render.blockentity.*;
import org.dfoodexpand.registry.ModBlockEntityTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityRendererFactories.class)
public abstract class BlockEntityRendererFactoriesMixin {
    @Shadow
    public static <T extends BlockEntity> void register(BlockEntityType<? extends T> type, BlockEntityRendererFactory<T> factory) {}

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void registerBlockEntityRenderers(CallbackInfo ci) {
        register(ModBlockEntityTypes.HANGING_HOOK, HangingHookBlockEntityRenderer::new);
        register(ModBlockEntityTypes.PICNIC_BASKET, PicnicBasketBlockEntityRenderer::new);
    }
}
