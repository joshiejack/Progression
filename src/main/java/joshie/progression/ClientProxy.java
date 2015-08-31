package joshie.progression;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.gui.base.DrawHelper;
import joshie.progression.gui.base.SaveTicker;
import joshie.progression.items.RenderItemCriteria;
import joshie.progression.json.Options;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy {
    @Override
    public void initClient() {
        if (Options.editor || Progression.NEI_LOADED) {
            FMLCommonHandler.instance().bus().register(new SaveTicker());
        }
        
        ProgressionAPI.draw = DrawHelper.INSTANCE;
        MinecraftForgeClient.registerItemRenderer(Progression.item, new RenderItemCriteria());
    }
}
