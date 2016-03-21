package joshie.progression;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.gui.GuiTreeEditor;
import joshie.progression.gui.base.DrawHelper;
import joshie.progression.gui.newversion.GuiConditionEditor;
import joshie.progression.gui.newversion.GuiCriteriaEditor;
import joshie.progression.gui.newversion.GuiGroupEditor;
import joshie.progression.gui.newversion.GuiItemFilterEditor;
import joshie.progression.helpers.RenderItemHelper;
import joshie.progression.items.RenderItemCriteria;
import joshie.progression.lib.GuiIDs;
import joshie.progression.lib.ProgressionInfo;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;

public class PClientProxy extends PCommonProxy {
	public static final ModelResourceLocation criteria = new ModelResourceLocation(new ResourceLocation(ProgressionInfo.MODPATH, "item"), "inventory");
	
    @Override
    public void initClient() {
        ProgressionAPI.draw = DrawHelper.INSTANCE;
        MinecraftForge.EVENT_BUS.register(new RenderItemCriteria());
    }
    
    private ModelResourceLocation getLocation(String name) {
    	return new ModelResourceLocation(new ResourceLocation(ProgressionInfo.MODPATH, name), "inventory");
    }
    
    @Override
    public void registerRendering() {
        RenderItemHelper.register(PCommonProxy.item, 0, criteria);
        RenderItemHelper.register(PCommonProxy.item, 1, getLocation("padlock"));
        RenderItemHelper.register(PCommonProxy.item, 2, getLocation("book"));
        ModelLoader.registerItemVariants(PCommonProxy.item, getLocation("padlock"), getLocation("book"));
    }
    
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == GuiIDs.CRITERIA) return GuiCriteriaEditor.INSTANCE;
        else if (ID == GuiIDs.CONDITION) return GuiConditionEditor.INSTANCE;
        else if (ID == GuiIDs.ITEM) return GuiItemFilterEditor.INSTANCE;
        else if (ID == GuiIDs.TREE) return GuiTreeEditor.INSTANCE;
        else if (ID == GuiIDs.GROUP) return GuiGroupEditor.INSTANCE;
        else return null;
    }
}