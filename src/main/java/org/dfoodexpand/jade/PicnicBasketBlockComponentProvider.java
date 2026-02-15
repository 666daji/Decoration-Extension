package org.dfoodexpand.jade;

import net.minecraft.block.BlockState;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.dfoodexpand.DFoodExpand;
import org.dfoodexpand.block.PicnicBasketBlock;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class PicnicBasketBlockComponentProvider implements IBlockComponentProvider {
    public static final Identifier ID = new Identifier(DFoodExpand.MOD_ID, "picnic_basket");

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        BlockState state = blockAccessor.getBlockState();
        boolean current = state.get(PicnicBasketBlock.IS_OPEN);
        boolean haveCarpet = state.getBlock() instanceof PicnicBasketBlock block && block.dyedCarpet != null;

        if (!current) {
            iTooltip.add(Text.translatable("jade.dfood_expand.basket.open"));
        }

        if (!haveCarpet && current) {
            iTooltip.add(Text.translatable("jade.dfood_expand.basket.add_carpet"));
        }

        if (haveCarpet && current) {
            iTooltip.add(Text.translatable("jade.dfood_expand.basket.remove_carpet"));
        }
    }

    @Override
    public Identifier getUid() {
        return ID;
    }
}
