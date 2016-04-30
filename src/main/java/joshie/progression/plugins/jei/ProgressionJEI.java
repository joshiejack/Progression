package joshie.progression.plugins.jei;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IRecipesGui;
import mezz.jei.api.JEIPlugin;

import javax.annotation.Nonnull;

@JEIPlugin
public class ProgressionJEI extends BlankModPlugin {
    public static IRecipesGui runtime;

    @Override
    public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime) {
        ProgressionJEI.runtime = jeiRuntime.getRecipesGui();
    }
}
