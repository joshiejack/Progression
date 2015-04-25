package joshie.crafting.trigger;

import java.util.ArrayList;
import java.util.List;

import joshie.crafting.api.Bus;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ITriggerData;
import joshie.crafting.api.ITriggerType;
import joshie.crafting.gui.fields.AbstractField;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.json.Theme;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.common.eventhandler.Event.Result;

public abstract class TriggerBase implements ITriggerType {
    protected List<AbstractField> list = new ArrayList();
    private String name;
    private int color;
    private String data;

    public TriggerBase(String name, int color, String data) {
        this.name = name;
        this.color = color;
        this.data = data;
    }

    @Override
    public ITriggerData newData() {
        return CraftingAPI.registry.newData(data);
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
    public Bus[] getEventBusTypes() {
        return new Bus[] { getEventBus() };
    }

    protected Bus getEventBus() {
        return Bus.FORGE;
    }

    public void addFields() {}

    @Override
    public Result onClicked(int mouseX, int mouseY) {
        if (ClientHelper.canEdit()) {
            int index = 0;
            for (AbstractField t : list) {
                t.setObject(this);
                if (t.attemptClick(mouseX, mouseY)) {
                    return Result.ALLOW;
                }

                int color = Theme.INSTANCE.optionsFontColor;
                int yPos = 17 + (index * 8);
                if (mouseX >= 1 && mouseX <= 99) {
                    if (mouseY >= yPos && mouseY < yPos + 8) {
                        t.click();
                        return Result.ALLOW;
                    }
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
            t.setObject(this);
            int color = Theme.INSTANCE.optionsFontColor;
            int yPos = 17 + (index * 8);
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
