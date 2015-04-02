package joshie.crafting;

import joshie.crafting.gui.EditorTicker;
import joshie.crafting.json.Options;
import cpw.mods.fml.common.FMLCommonHandler;

public class CraftingClient extends CraftingCommon {
    @Override
    public void initClient() {
        if (Options.editor || CraftingMod.NEI_LOADED) {
            FMLCommonHandler.instance().bus().register(new EditorTicker());
        }
    }
}
