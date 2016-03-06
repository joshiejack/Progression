package joshie.progression.jei;

import mezz.jei.api.*;

@JEIPlugin
public class JEISupport extends BlankModPlugin {
    public static IJeiHelpers helpers;
    public static IItemRegistry itemRegistry;

    @Override
    public void register(IModRegistry registry) {
        helpers = registry.getJeiHelpers();
        itemRegistry = registry.getItemRegistry();
    }
}