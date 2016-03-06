package joshie.progression.criteria.triggers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.JsonObject;

import joshie.progression.api.EventBusType;
import joshie.progression.api.ICriteria;
import joshie.progression.api.ITriggerData;
import joshie.progression.api.ITriggerType;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.gui.fields.AbstractField;
import joshie.progression.handlers.APIHandler;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.helpers.JSONHelper;
import joshie.progression.json.Theme;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public abstract class TriggerBase implements ITriggerType {
    protected List<AbstractField> list = new ArrayList();
    protected ICriteria criteria;
    private String name;
    private int color;
    private String data;
    protected boolean cancelable = false;
    public boolean cancel = false;

    public TriggerBase(String name, int color, String data) {
        this.name = name;
        this.color = color;
        this.data = data;
    }

    @Override
    public ITriggerData newData() {
        return APIHandler.newData(data);
    }

    @Override
    public void markCriteria(ICriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public String getUnlocalisedName() {
        return name;
    }

    @Override
    public String getLocalisedName() {
        return StatCollector.translateToLocal("progression.trigger." + getUnlocalisedName());
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public EventBusType[] getEventBusTypes() {
        return new EventBusType[] { getEventBus() };
    }

    protected EventBusType getEventBus() {
        return EventBusType.FORGE;
    }

    @Override
    public boolean onFired(UUID uuid, ITriggerData existing, Object... additional) {
        if (cancelable) return cancel ? false : true;
        else return true;
    }

    @Override
    public void readFromJSON(JsonObject data) {
        if (cancelable) cancel = JSONHelper.getBoolean(data, "cancel", cancel);
    }

    @Override
    public void writeToJSON(JsonObject data) {
        if (cancelable) JSONHelper.setBoolean(data, "cancel", cancel, false);
    }

    @Override
    public Result onClicked(int mouseX, int mouseY) {
        if (MCClientHelper.canEdit()) {
            if (!cancel) {
                int yStart = cancelable ? 25 : 17;
                int index = 0;
                for (AbstractField t : list) {
                    t.setObject(this);
                    if (t.attemptClick(mouseX, mouseY)) {
                        return Result.ALLOW;
                    }

                    int color = Theme.INSTANCE.optionsFontColor;
                    int yPos = yStart + (index * 8);
                    if (mouseX >= 1 && mouseX <= 99) {
                        if (mouseY >= yPos && mouseY < yPos + 8) {
                            t.click();
                            return Result.ALLOW;
                        }
                    }

                    index++;
                }
            }

            if (cancelable) {
                if (mouseX >= 1 && mouseX <= 84) {
                    if (mouseY >= 17 && mouseY < 25) {
                        cancel = !cancel;
                    }
                }
            }
        }

        return Result.DEFAULT;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (!cancel) {
            int yStart = cancelable ? 25 : 17;
            int index = 0;
            for (AbstractField t : list) {
                t.setObject(this);
                int color = Theme.INSTANCE.optionsFontColor;
                int yPos = yStart + (index * 8);
                if (MCClientHelper.canEdit()) {
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

        if (cancelable) {
            int color = Theme.INSTANCE.optionsFontColor;
            if (MCClientHelper.canEdit()) {
                if (mouseX >= 1 && mouseX <= 84) {
                    if (mouseY >= 17 && mouseY < 25) {
                        color = Theme.INSTANCE.optionsFontColorHover;
                    }
                }

                ProgressionAPI.draw.drawSplitText("cancel: " + cancel, 4, 17, 105, color);
            }
        }
    }
}
