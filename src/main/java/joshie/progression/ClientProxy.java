package joshie.progression;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.gui.base.DrawHelper;
import joshie.progression.gui.base.SaveTicker;
import joshie.progression.json.Options;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
    @Override
    public void initClient() {
        if (Options.editor || Progression.JEI_LOADED) {
            MinecraftForge.EVENT_BUS.register(new SaveTicker());
        }

        ProgressionAPI.draw = DrawHelper.INSTANCE;
        //TODO: Render Criteria MinecraftForgeClient.registerItemRenderer(Progression.item, new RenderItemCriteria());
    }
}