package joshie.crafting;

import joshie.crafting.gui.EditorTicker;
import cpw.mods.fml.common.FMLCommonHandler;

public class CraftingClient extends CraftingCommon {
    @Override
    public void initClient() {
        if (CraftingMod.options.editor) {
            FMLCommonHandler.instance().bus().register(new EditorTicker());
        }
    }
}
