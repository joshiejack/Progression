package joshie.crafting.trigger;

import java.util.List;

import joshie.crafting.api.Bus;
import joshie.crafting.api.IResearch;
import joshie.crafting.api.ITrigger;
import joshie.crafting.gui.TextFieldHelper;
import joshie.crafting.helpers.ClientHelper;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.Event.Result;

public class TriggerResearch extends TriggerBaseBoolean implements IResearch {
    private TextFieldHelper nameEdit;

    public String researchName = "dummy";

    public TriggerResearch() {
        super("Research", theme.triggerResearch, "research");
        nameEdit = new TextFieldHelper("researchName", this);
    }

    @Override
    public ITrigger newInstance() {
        return new TriggerResearch();
    }

    @Override
    public Bus getBusType() {
        return Bus.NONE;
    }

    @Override
    public ITrigger deserialize(JsonObject data) {
        TriggerResearch trigger = new TriggerResearch();
        trigger.researchName = data.get("researchName").getAsString();
        return trigger;
    }

    @Override
    public void serialize(JsonObject data) {
        data.addProperty("researchName", researchName);
    }

    @Override
    public String getResearchName() {
        return researchName;
    }

    @Override
    protected boolean isTrue(Object... data) {
        return asString(data).equals(researchName);
    }

    @Override
    public Result clicked() {
        if (mouseX <= 84 && mouseX >= 1) {
            if (mouseY >= 17 && mouseY <= 33) nameEdit.select();
            if (mouseY >= 17 && mouseY <= 33) return Result.ALLOW;
        }

        return Result.DEFAULT;
    }

    @Override
    public void draw() {
        int color = theme.optionsFontColor;
        if (ClientHelper.canEdit()) {
            if (mouseX <= 84 && mouseX >= 1) {
                if (mouseY >= 17 && mouseY <= 33) color = theme.optionsFontColorHover;
            }
        }

        drawText("researchName: ", 4, 18, color);
        drawText("" + nameEdit, 4, 26, color);
    }
    
    @Override
    public void addTooltip(List<String> toolTip) {
        toolTip.add("Have " + researchName + " research");
    }
}
