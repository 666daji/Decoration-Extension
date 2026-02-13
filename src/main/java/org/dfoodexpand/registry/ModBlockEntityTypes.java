package org.dfoodexpand.registry;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.dfood.ThreedFood;
import org.dfoodexpand.block.PicnicBasketBlock;
import org.dfoodexpand.block.entity.HangingHookBlockEntity;
import org.dfoodexpand.block.entity.PicnicBasketBlockEntity;

public class ModBlockEntityTypes {
    public static final BlockEntityType<HangingHookBlockEntity> HANGING_HOOK = register("hanging_hook", BlockEntityType.Builder.create(
            HangingHookBlockEntity::new, ModBlocks.WOODEN_HANGING_HOOK, ModBlocks.COPPER_HANGING_HOOK,
            ModBlocks.WOODEN_RACK_HANGING_HOOK, ModBlocks.COPPER_RACK_HANGING_HOOK
    ));
    public static final BlockEntityType<PicnicBasketBlockEntity> PICNIC_BASKET = register("picnic_basket", BlockEntityType.Builder.create(
            PicnicBasketBlockEntity::new, PicnicBasketBlock.getAll()
    ));

    private static <T extends BlockEntity> BlockEntityType<T> register(String id, BlockEntityType.Builder<T> builder) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(ThreedFood.MOD_ID, id), builder.build(null));
    }

    public static void registerAll() {}
}
