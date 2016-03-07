package joshie.progression;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.gui.base.DrawHelper;
import joshie.progression.gui.base.SaveTicker;
import joshie.progression.helpers.RenderItemHelper;
import joshie.progression.items.RenderItemCriteria;
import joshie.progression.json.Options;
import joshie.progression.lib.ProgressionInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
	public static final ModelResourceLocation criteria = new ModelResourceLocation(new ResourceLocation(ProgressionInfo.MODPATH, "item"), "inventory");
	
    @Override
    public void initClient() {
        if (Options.editor || Progression.JEI_LOADED) {
            MinecraftForge.EVENT_BUS.register(new SaveTicker());
        }

        ProgressionAPI.draw = DrawHelper.INSTANCE;
        MinecraftForge.EVENT_BUS.register(new RenderItemCriteria());
    }
    
    private ModelResourceLocation getLocation(String name) {
    	return new ModelResourceLocation(new ResourceLocation(ProgressionInfo.MODPATH, name), "inventory");
    }
    
    @Override
    public void registerRendering() {
        RenderItemHelper.register(Progression.item, 0, criteria);
        RenderItemHelper.register(Progression.item, 1, getLocation("padlock"));
        RenderItemHelper.register(Progression.item, 2, getLocation("book"));
        ModelLoader.registerItemVariants(Progression.item, getLocation("padlock"), getLocation("book"));
    }
}