package joshie.progression.plugins.enchiridion.actions;

import com.google.gson.JsonObject;

import joshie.enchiridion.api.book.IButtonAction;
import joshie.progression.Progression;
import joshie.progression.helpers.JSONHelper;
import joshie.progression.lib.ProgressionInfo;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractAction implements IButtonAction {
    private transient ResourceLocation hovered;
    private transient ResourceLocation unhovered;
    private transient String name;
    public transient String tooltip = "";
    public transient String hoverText = "";
    public transient String unhoveredText = "";
    private transient boolean isInit;
    
    public AbstractAction() {}
    public AbstractAction(String name) {
        this.hovered = new ResourceLocation(ProgressionInfo.BOOKPATH + name.replace('.', '_') + "_hover.png");
        this.unhovered = new ResourceLocation(ProgressionInfo.BOOKPATH + name.replace('.', '_') + "_dftl.png");
        this.name = name;
    }
    
    @Override
    public String[] getFieldNames() {
        return new String[] { "tooltip", "hoverText", "unhoveredText" };
    }
    
    @Override
    public String getTooltip() {
        return tooltip;
    }
    
    @Override
    public void readFromJson(JsonObject object) {
        tooltip = JSONHelper.getString(object, "tooltip", "");
        hoverText = JSONHelper.getString(object, "hoverText", "");
        unhoveredText = JSONHelper.getString(object, "unhoveredText", "");
    }

    @Override
    public void writeToJson(JsonObject object) {
        if (tooltip != null && !tooltip.equals("")) object.addProperty("tooltip", tooltip);
        if (hoverText != null && !hoverText.equals("")) object.addProperty("hoverText", hoverText);
        if (unhoveredText != null && !unhoveredText.equals("")) object.addProperty("unhoveredText", unhoveredText);
    }
    
    @Override
    public String getName() {
        return Progression.translate("action." + name);
    }
    
    @Override
    public String getHoverText() {
        if (hoverText == null) hoverText = "";
        return hoverText;
    }
    
    @Override
    public String getUnhoverText() { //Because why not?, TODO: ADD INIT FOR ACTIONS TO ENCHIRIDION API
        if (unhoveredText == null) unhoveredText = "";
        return unhoveredText;
    }
    
    @Override
    public ResourceLocation getHovered() {
        return hovered;
    }
    
    @Override
    public ResourceLocation getUnhovered() {
        return unhovered;
    }
}
