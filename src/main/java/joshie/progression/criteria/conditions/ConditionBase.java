package joshie.progression.criteria.conditions;

import java.util.ArrayList;
import java.util.List;

import joshie.progression.api.IConditionType;
import joshie.progression.gui.fields.AbstractField;
import joshie.progression.helpers.ClientHelper;
import joshie.progression.json.Theme;
import net.minecraft.util.StatCollector;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.Event.Result;

public abstract class ConditionBase implements IConditionType {
    protected List<AbstractField> list = new ArrayList();
    private String name;
    private int color;

    public ConditionBase(String name, int color) {
        this.name = name;
        this.color = color;
    }

    @Override
    public String getUnlocalisedName() {
        return name;
    }

    @Override
    public String getLocalisedName() {
        return StatCollector.translateToLocal("progression.condition." + getUnlocalisedName());
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public void readFromJSON(JsonObject data) {}

    @Override
    public void writeToJSON(JsonObject data) {}

    @Override
    public Result onClicked(int mouseX, int mouseY) {
        if (ClientHelper.canEdit()) {
            int index = 0;
            for (AbstractField t : list) {
                int color = Theme.INSTANCE.optionsFontColor;
                int yPos = 25 + (index * 8);
                if (mouseX >= 1 && mouseX <= 84) {
                    if (mouseY >= yPos && mouseY < yPos + 8) {
                        t.click();
                        return Result.ALLOW;
                    }
                }

                if (t.attemptClick(mouseX, mouseY)) {
                    return Result.ALLOW;
                }

                index++;
            }
        }

        return Result.DEFAULT;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        int index = 0;
        for (AbstractField t : list) {
            int color = Theme.INSTANCE.optionsFontColor;
            int yPos = 25 + (index * 8);
            if (ClientHelper.canEdit()) {
                if (mouseX >= 1 && mouseX <= 84) {
                    if (mouseY >= yPos && mouseY < yPos + 8) {
                        color = Theme.INSTANCE.optionsFontColorHover;
                    }
                }
            }

            t.draw(color, yPos);
            index++;
        }
    }
}
