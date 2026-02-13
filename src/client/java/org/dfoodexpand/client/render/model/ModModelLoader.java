package org.dfoodexpand.client.render.model;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.dfoodexpand.block.entity.PicnicBasketBlockEntity;

import java.util.ArrayList;
import java.util.List;

public class ModModelLoader implements ModelLoadingPlugin {
    /**
     * 存储所有需要加载的模型标识符。
     */
    private static final List<Identifier> MODELS_TO_LOAD = new ArrayList<>();

    @Override
    public void onInitializeModelLoader(Context context) {
        registryAllBasket();

        context.addModels(MODELS_TO_LOAD.toArray(new Identifier[0]));
    }

    private static void registryAllBasket() {
        for (Item item : PicnicBasketBlockEntity.CAN_PLACE_COUNT.keySet()) {
            int i = PicnicBasketBlockEntity.CAN_PLACE_COUNT.get(item);
            for (int l = 1; l < i + 1; l++) {
                MODELS_TO_LOAD.add(createBasketPlaceModel(item, l));
            }
        }
    }

    /**
     * 创建放置在野餐篮中的模型标识符。
     *
     * @param item 放置的物品
     * @param count 放置的数量
     * @return 对应的模型标识符
     */
    public static Identifier createBasketPlaceModel(Item item, int count) {
        String nameSpace = Registries.ITEM.getId(item).getNamespace();
        String itemId = Registries.ITEM.getId(item).getPath();

        return new Identifier(nameSpace, "basket/" + itemId + "_" + count);
    }
}
