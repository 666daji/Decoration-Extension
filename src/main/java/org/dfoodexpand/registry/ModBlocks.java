package org.dfoodexpand.registry;

import net.minecraft.block.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.dfoodexpand.DFoodExpand;
import org.dfoodexpand.block.*;

public class ModBlocks {
    public static final Block IRON_HANGING_HOOK = register("iron_hanging_hook", new HangingHookBlock(AbstractBlock.Settings.create()
            .mapColor(MapColor.OAK_TAN).strength(1.0F, 3.0F).sounds(BlockSoundGroup.COPPER).nonOpaque()));
    public static final Block COPPER_HANGING_HOOK = register("copper_hanging_hook", new HangingHookBlock(AbstractBlock.Settings.create()
            .mapColor(MapColor.ORANGE).strength(1.0F, 6.0F).sounds(BlockSoundGroup.COPPER).nonOpaque()));
    public static final Block IRON_RACK_HANGING_HOOK = register("iron_rack_hanging_hook", new HangingHookBlock(AbstractBlock.Settings.create()
            .mapColor(MapColor.OAK_TAN).strength(1.0F, 3.0F).sounds(BlockSoundGroup.WOOD).nonOpaque()));
    public static final Block COPPER_RACK_HANGING_HOOK = register("copper_rack_hanging_hook", new HangingHookBlock(AbstractBlock.Settings.create()
            .mapColor(MapColor.ORANGE).strength(1.0F, 6.0F).sounds(BlockSoundGroup.WOOD).nonOpaque()));
    public static final Block PICNIC_BASKET = register("picnic_basket", new PicnicBasketBlock(AbstractBlock.Settings.create()
            .mapColor(MapColor.OAK_TAN).strength(1.0F, 2.0F).sounds(BlockSoundGroup.WOOD).nonOpaque(), null));
    public static final Block WHITE_PICNIC_BASKET = register("white_picnic_basket", new PicnicBasketBlock(AbstractBlock.Settings.copy(PICNIC_BASKET),
            (DyedCarpetBlock) Blocks.WHITE_CARPET));
    public static final Block ORANGE_PICNIC_BASKET = register("orange_picnic_basket", new PicnicBasketBlock(AbstractBlock.Settings.copy(PICNIC_BASKET),
            (DyedCarpetBlock) Blocks.ORANGE_CARPET));
    public static final Block MAGENTA_PICNIC_BASKET = register("magenta_picnic_basket", new PicnicBasketBlock(AbstractBlock.Settings.copy(PICNIC_BASKET),
            (DyedCarpetBlock) Blocks.MAGENTA_CARPET));
    public static final Block LIGHT_BLUE_PICNIC_BASKET = register("light_blue_picnic_basket", new PicnicBasketBlock(AbstractBlock.Settings.copy(PICNIC_BASKET),
            (DyedCarpetBlock) Blocks.LIGHT_BLUE_CARPET));
    public static final Block YELLOW_PICNIC_BASKET = register("yellow_picnic_basket", new PicnicBasketBlock(AbstractBlock.Settings.copy(PICNIC_BASKET),
            (DyedCarpetBlock) Blocks.YELLOW_CARPET));
    public static final Block LIME_PICNIC_BASKET = register("lime_picnic_basket", new PicnicBasketBlock(AbstractBlock.Settings.copy(PICNIC_BASKET),
            (DyedCarpetBlock) Blocks.LIME_CARPET));
    public static final Block PINK_PICNIC_BASKET = register("pink_picnic_basket", new PicnicBasketBlock(AbstractBlock.Settings.copy(PICNIC_BASKET),
            (DyedCarpetBlock) Blocks.PINK_CARPET));
    public static final Block GRAY_PICNIC_BASKET = register("gray_picnic_basket", new PicnicBasketBlock(AbstractBlock.Settings.copy(PICNIC_BASKET),
            (DyedCarpetBlock) Blocks.GRAY_CARPET));
    public static final Block LIGHT_GRAY_PICNIC_BASKET = register("light_gray_picnic_basket", new PicnicBasketBlock(AbstractBlock.Settings.copy(PICNIC_BASKET),
            (DyedCarpetBlock) Blocks.LIGHT_GRAY_CARPET));
    public static final Block CYAN_PICNIC_BASKET = register("cyan_picnic_basket", new PicnicBasketBlock(AbstractBlock.Settings.copy(PICNIC_BASKET),
            (DyedCarpetBlock) Blocks.CYAN_CARPET));
    public static final Block PURPLE_PICNIC_BASKET = register("purple_picnic_basket", new PicnicBasketBlock(AbstractBlock.Settings.copy(PICNIC_BASKET),
            (DyedCarpetBlock) Blocks.PURPLE_CARPET));
    public static final Block BLUE_PICNIC_BASKET = register("blue_picnic_basket", new PicnicBasketBlock(AbstractBlock.Settings.copy(PICNIC_BASKET),
            (DyedCarpetBlock) Blocks.BLUE_CARPET));
    public static final Block BROWN_PICNIC_BASKET = register("brown_picnic_basket", new PicnicBasketBlock(AbstractBlock.Settings.copy(PICNIC_BASKET),
            (DyedCarpetBlock) Blocks.BROWN_CARPET));
    public static final Block GREEN_PICNIC_BASKET = register("green_picnic_basket", new PicnicBasketBlock(AbstractBlock.Settings.copy(PICNIC_BASKET),
            (DyedCarpetBlock) Blocks.GREEN_CARPET));
    public static final Block RED_PICNIC_BASKET = register("red_picnic_basket", new PicnicBasketBlock(AbstractBlock.Settings.copy(PICNIC_BASKET),
            (DyedCarpetBlock) Blocks.RED_CARPET));
    public static final Block BLACK_PICNIC_BASKET = register("black_picnic_basket", new PicnicBasketBlock(AbstractBlock.Settings.copy(PICNIC_BASKET),
            (DyedCarpetBlock) Blocks.BLACK_CARPET));

    private static Block register(String id, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(DFoodExpand.MOD_ID, id), block);
    }

    public static void registerAll() {}
}
