package joshie.progression.jei;

import mezz.jei.api.IItemRegistry;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.JEIPlugin;

@JEIPlugin
public class JEISupport implements IModPlugin {
	public static IJeiHelpers helpers;
	public static IItemRegistry registry;
	
    @Override
    public void onJeiHelpersAvailable(IJeiHelpers jeiHelpers) {
    	helpers = jeiHelpers;
    }

    @Override
    public void onItemRegistryAvailable(IItemRegistry itemRegistry) {}

    @Override
    public void register(IModRegistry registry) {}

    @Override
    public void onRecipeRegistryAvailable(IRecipeRegistry recipeRegistry) {}

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {}
}