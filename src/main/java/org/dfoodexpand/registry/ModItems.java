package org.dfoodexpand.registry;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.dfoodexpand.DFoodExpand;

public class ModItems {
    // 挂钩
    public static final Item IRON_HANGING_HOOK = registerItem(ModBlocks.IRON_HANGING_HOOK, new Item.Settings());
    public static final Item COPPER_HANGING_HOOK = registerItem(ModBlocks.COPPER_HANGING_HOOK, new Item.Settings());
    public static final Item IRON_RACK_HANGING_HOOK = registerItem(ModBlocks.IRON_RACK_HANGING_HOOK, new Item.Settings());
    public static final Item COPPER_RACK_HANGING_HOOK = registerItem(ModBlocks.COPPER_RACK_HANGING_HOOK, new Item.Settings());

    // 野餐篮
    public static final Item PICNIC_BASKET = registerItem(ModBlocks.PICNIC_BASKET, new Item.Settings().maxCount(1));
    public static final Item WHITE_PICNIC_BASKET = registerItem(ModBlocks.WHITE_PICNIC_BASKET, new Item.Settings().maxCount(1));
    public static final Item ORANGE_PICNIC_BASKET = registerItem(ModBlocks.ORANGE_PICNIC_BASKET, new Item.Settings().maxCount(1));
    public static final Item MAGENTA_PICNIC_BASKET = registerItem(ModBlocks.MAGENTA_PICNIC_BASKET, new Item.Settings().maxCount(1));
    public static final Item LIGHT_BLUE_PICNIC_BASKET = registerItem(ModBlocks.LIGHT_BLUE_PICNIC_BASKET, new Item.Settings().maxCount(1));
    public static final Item YELLOW_PICNIC_BASKET = registerItem(ModBlocks.YELLOW_PICNIC_BASKET, new Item.Settings().maxCount(1));
    public static final Item LIME_PICNIC_BASKET = registerItem(ModBlocks.LIME_PICNIC_BASKET, new Item.Settings().maxCount(1));
    public static final Item PINK_PICNIC_BASKET = registerItem(ModBlocks.PINK_PICNIC_BASKET, new Item.Settings().maxCount(1));
    public static final Item GRAY_PICNIC_BASKET = registerItem(ModBlocks.GRAY_PICNIC_BASKET, new Item.Settings().maxCount(1));
    public static final Item LIGHT_GRAY_PICNIC_BASKET = registerItem(ModBlocks.LIGHT_GRAY_PICNIC_BASKET, new Item.Settings().maxCount(1));
    public static final Item CYAN_PICNIC_BASKET = registerItem(ModBlocks.CYAN_PICNIC_BASKET, new Item.Settings().maxCount(1));
    public static final Item PURPLE_PICNIC_BASKET = registerItem(ModBlocks.PURPLE_PICNIC_BASKET, new Item.Settings().maxCount(1));
    public static final Item BLUE_PICNIC_BASKET = registerItem(ModBlocks.BLUE_PICNIC_BASKET, new Item.Settings().maxCount(1));
    public static final Item BROWN_PICNIC_BASKET = registerItem(ModBlocks.BROWN_PICNIC_BASKET, new Item.Settings().maxCount(1));
    public static final Item GREEN_PICNIC_BASKET = registerItem(ModBlocks.GREEN_PICNIC_BASKET, new Item.Settings().maxCount(1));
    public static final Item RED_PICNIC_BASKET = registerItem(ModBlocks.RED_PICNIC_BASKET, new Item.Settings().maxCount(1));
    public static final Item BLACK_PICNIC_BASKET = registerItem(ModBlocks.BLACK_PICNIC_BASKET, new Item.Settings().maxCount(1));

    // 椅子
    public static final Item BIRCH_CHAIR = registerItem(ModBlocks.BIRCH_CHAIR, new Item.Settings().maxCount(1));
    public static final Item JUNGLE_CHAIR = registerItem(ModBlocks.JUNGLE_CHAIR, new Item.Settings().maxCount(1));
    public static final Item ACACIA_CHAIR = registerItem(ModBlocks.ACACIA_CHAIR, new Item.Settings().maxCount(1));
    public static final Item OAK_CHAIR = registerItem(ModBlocks.OAK_CHAIR, new Item.Settings().maxCount(1));
    public static final Item DARK_OAK_CHAIR = registerItem(ModBlocks.DARK_OAK_CHAIR, new Item.Settings().maxCount(1));
    public static final Item CHERRY_CHAIR = registerItem(ModBlocks.CHERRY_CHAIR, new Item.Settings().maxCount(1));
    public static final Item SPRUCE_CHAIR = registerItem(ModBlocks.SPRUCE_CHAIR, new Item.Settings().maxCount(1));
    public static final Item BAMBOO_CHAIR = registerItem(ModBlocks.BAMBOO_CHAIR, new Item.Settings().maxCount(1));

    // 钩子
    public static final Item HOOK = registerItem(ModBlocks.HOOK, new Item.Settings());

    private static Item registerItem(Block block){
        return registerItem(block, new Item.Settings());
    }

    private static Item registerItem(Block block, Item.Settings settings){
        return registerItem(Registries.BLOCK.getId(block).getPath(), new BlockItem(block, settings));
    }

    public static Item registerItem(String name, Item item) {
        if (item instanceof BlockItem blockItem) {
            blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
        }

        return Registry.register(Registries.ITEM, new Identifier(DFoodExpand.MOD_ID, name), item);
    }

    public static void registerAll() {}
}
