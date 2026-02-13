package org.dfoodexpand;

import net.fabricmc.api.ModInitializer;
import org.dfoodexpand.registry.RegistryInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DFoodExpand implements ModInitializer {
    public static final String MOD_ID = "dfood_expand";
    public static final Logger LOGGER = LoggerFactory.getLogger("Tw`s Decoration Extension");

    @Override
    public void onInitialize() {
        RegistryInit.init();
        LOGGER.info("Decorative Food Expand is initializing!");
    }
}
