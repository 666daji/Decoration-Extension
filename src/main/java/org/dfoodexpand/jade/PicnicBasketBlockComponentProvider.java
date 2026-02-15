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
    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        BlockState state = blockAccessor.getBlockState();
        boolean current = state.get(PicnicBasketBlock.IS_OPEN);
        boolean haveCarpet = state.getBlock() instanceof PicnicBasketBlock block && block.dyedCarpet != null;

        if (!current) {
            iTooltip.add(Text.translatable("basket.open"));
        }

        if (!haveCarpet && current) {
            iTooltip.add(Text.translatable("basket.add_carpet"));
        }

        if (haveCarpet && current) {
            iTooltip.add(Text.translatable("basket.remove_carpet"));
        }
    }

    @Override
    public Identifier getUid() {
        return new Identifier(DFoodExpand.MOD_ID, "picnic_basket");
    }
}
