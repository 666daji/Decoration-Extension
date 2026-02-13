package org.dfoodexpand.registry;

public class RegistryInit {
    public static void init() {
        ModBlocks.registerAll();
        ModItems.registerAll();
        ModBlockEntityTypes.registerAll();
        ModItemGroups.RegistryModItemGroups();
    }
}
