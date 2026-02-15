package org.dfoodexpand.jade;

import org.dfoodexpand.block.PicnicBasketBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class Plugins implements IWailaPlugin {

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(new PicnicBasketBlockComponentProvider(), PicnicBasketBlock.class);
    }
}
