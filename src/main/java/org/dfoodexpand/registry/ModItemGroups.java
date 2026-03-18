package org.dfoodexpand.registry;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.dfoodexpand.DFoodExpand;

public class ModItemGroups {
    public static void RegistryModItemGroups(){
        Registry.register(
                Registries.ITEM_GROUP,
                new Identifier(DFoodExpand.MOD_ID, "dfood_expand_group"),
                ItemGroup.create(ItemGroup.Row.TOP, -1)
                        .displayName(Text.translatable("itemgroup.dfood_expand"))
                        .icon(() -> new ItemStack(Items.BREAD))
                        .entries(((displayContext, entries) -> {
                            entries.add(ModItems.IRON_HANGING_HOOK);
                            entries.add(ModItems.COPPER_HANGING_HOOK);
                            entries.add(ModItems.IRON_RACK_HANGING_HOOK);
                            entries.add(ModItems.COPPER_RACK_HANGING_HOOK);
                            entries.add(ModItems.PICNIC_BASKET);
                            entries.add(ModItems.WHITE_PICNIC_BASKET);
                            entries.add(ModItems.ORANGE_PICNIC_BASKET);
                            entries.add(ModItems.MAGENTA_PICNIC_BASKET);
                            entries.add(ModItems.LIGHT_BLUE_PICNIC_BASKET);
                            entries.add(ModItems.YELLOW_PICNIC_BASKET);
                            entries.add(ModItems.LIME_PICNIC_BASKET);
                            entries.add(ModItems.PINK_PICNIC_BASKET);
                            entries.add(ModItems.GRAY_PICNIC_BASKET);
                            entries.add(ModItems.LIGHT_GRAY_PICNIC_BASKET);
                            entries.add(ModItems.CYAN_PICNIC_BASKET);
                            entries.add(ModItems.PURPLE_PICNIC_BASKET);
                            entries.add(ModItems.BLUE_PICNIC_BASKET);
                            entries.add(ModItems.BROWN_PICNIC_BASKET);
                            entries.add(ModItems.GREEN_PICNIC_BASKET);
                            entries.add(ModItems.RED_PICNIC_BASKET);
                            entries.add(ModItems.BLACK_PICNIC_BASKET);

                            entries.add(ModItems.BIRCH_CHAIR);
                            entries.add(ModItems.JUNGLE_CHAIR);
                            entries.add(ModItems.ACACIA_CHAIR);
                            entries.add(ModItems.OAK_CHAIR);
                            entries.add(ModItems.DARK_OAK_CHAIR);
                            entries.add(ModItems.CHERRY_CHAIR);
                            entries.add(ModItems.SPRUCE_CHAIR);
                            entries.add(ModItems.BAMBOO_CHAIR);

                            entries.add(ModItems.HOOK);
                        }))
                        .build()
        );
    }
}