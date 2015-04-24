package joshie.crafting;

import joshie.crafting.api.DrawHelper;
import joshie.crafting.gui.EditorTicker;
import joshie.crafting.gui.GuiDrawHelper;
import joshie.crafting.json.Options;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.common.FMLCommonHandler;

public class CraftingClient extends CraftingCommon {
    @Override
    public void initClient() {
        if (Options.editor || CraftingMod.NEI_LOADED) {
            FMLCommonHandler.instance().bus().register(new EditorTicker());
        }
        
        DrawHelper.triggerDraw = GuiDrawHelper.TriggerDrawHelper.INSTANCE;
        
        MinecraftForgeClient.registerItemRenderer(CraftingMod.item, new RenderItemCriteria());
    }
}
