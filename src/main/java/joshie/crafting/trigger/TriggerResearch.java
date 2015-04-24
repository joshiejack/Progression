package joshie.crafting.trigger;

import joshie.crafting.api.Bus;
import joshie.crafting.api.DrawHelper;
import joshie.crafting.api.IResearch;
import joshie.crafting.gui.TextFieldHelper;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.JSONHelper;
import joshie.crafting.json.Theme;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.Event.Result;

public class TriggerResearch extends TriggerBaseBoolean implements IResearch {
    private TextFieldHelper nameEdit;

    public String researchName = "dummy";

    public TriggerResearch() {
        super("research", 0xFF26C9FF);
        nameEdit = new TextFieldHelper("researchName", this);
    }

    @Override
    public Bus getEventBus() {
        return Bus.NONE;
    }

    @Override
    public void readFromJSON (JsonObject data) {
        researchName = JSONHelper.getString(data, "researchName", researchName);
    }

    @Override
    public void writeToJSON(JsonObject data) {
        JSONHelper.setString(data, "researchName", researchName, "dummy");
    }

    @Override
    public String getResearchName() {
        return researchName;
    }

    @Override
    protected boolean isTrue(Object... data) {
        return ((String)data[0]).equals(researchName);
    }

    @Override
    public Result onClicked(int mouseX, int mouseY) {
        if (mouseX <= 84 && mouseX >= 1) {
            if (mouseY >= 17 && mouseY <= 33) nameEdit.select();
            if (mouseY >= 17 && mouseY <= 33) return Result.ALLOW;
        }

        return Result.DEFAULT;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        int color = Theme.INSTANCE.optionsFontColor;
        if (ClientHelper.canEdit()) {
            if (mouseX <= 84 && mouseX >= 1) {
                if (mouseY >= 17 && mouseY <= 33) color = Theme.INSTANCE.optionsFontColorHover;
            }
        }

        DrawHelper.triggerDraw.drawText("researchName: ", 4, 18, color);
        DrawHelper.triggerDraw.drawText("" + nameEdit, 4, 26, color);
    }
}
