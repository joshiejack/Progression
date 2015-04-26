package joshie.progression.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEIConfig implements IConfigureNEI {

    @Override
    public void loadConfig() {
        API.registerRecipeHandler(new NEIOverrideHandler());
        API.registerUsageHandler(new NEIOverrideHandler());
    }

    @Override
    public String getName() {
        return "Progression NEI";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }
}
