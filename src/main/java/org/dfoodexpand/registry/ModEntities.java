package org.dfoodexpand.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.dfoodexpand.DFoodExpand;
import org.dfoodexpand.entity.ChairEntity;

public class ModEntities {
    public static final EntityType<ChairEntity> CHAIR = register("chair", FabricEntityTypeBuilder.<ChairEntity>create(SpawnGroup.MISC, (ChairEntity::new))
            .dimensions(EntityDimensions.fixed(0.0f, 0.0f)) // 无碰撞箱
            .trackRangeBlocks(10)
            .trackedUpdateRate(1)
            .build());

    private static <T extends Entity> EntityType<T> register(String id, EntityType<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, new Identifier(DFoodExpand.MOD_ID, id), type);
    }

    public static void registerAll() {}
}
