package org.dfoodexpand.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.dfoodexpand.DFoodExpand;

public class ModTags {
    public static final TagKey<Item> HANGABLE_ITEMS = ofItem("hangable_items");

    private static TagKey<Item> ofItem(String id) {
        return TagKey.of(RegistryKeys.ITEM, new Identifier(DFoodExpand.MOD_ID, id));
    }

    private static TagKey<Block> ofBlock(String id) {
        return TagKey.of(RegistryKeys.BLOCK, new Identifier(DFoodExpand.MOD_ID, id));
    }
}
