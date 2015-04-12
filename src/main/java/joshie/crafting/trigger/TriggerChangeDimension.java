package joshie.crafting.trigger;

import java.util.List;

import joshie.crafting.api.Bus;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ITrigger;
import joshie.crafting.gui.TextFieldHelper.IntegerFieldHelper;
import joshie.crafting.helpers.ClientHelper;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;

public class TriggerChangeDimension extends TriggerBaseCounter {
    private IntegerFieldHelper amountEdit;
    private IntegerFieldHelper fromEdit;
    private IntegerFieldHelper toEdit;
    public int amount = 1;
    public boolean checkFrom = false;
    public int from = 0;
    public boolean checkTo = true;
    public int to = -1;

    public TriggerChangeDimension() {
        super("Change Dimension", theme.triggerChangeDimension, "changeDimension");
        amountEdit = new IntegerFieldHelper("amount", this);
        fromEdit = new IntegerFieldHelper("from", this);
        toEdit = new IntegerFieldHelper("to", this);
    }

    @Override
    public ITrigger newInstance() {
        return new TriggerChangeDimension();
    }

    @Override
    public Bus getBusType() {
        return Bus.FML;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEvent(PlayerChangedDimensionEvent event) {
        CraftingAPI.registry.fireTrigger(event.player, getTypeName(), event.fromDim, event.toDim);
    }

    @Override
    public ITrigger deserialize(JsonObject data) {
        TriggerChangeDimension trigger = new TriggerChangeDimension();
        if (data.get("amount") != null) {
            trigger.amount = data.get("amount").getAsInt();
        }

        trigger.checkFrom = false;
        if (data.get("from") != null) {
            trigger.checkFrom = true;
            trigger.from = data.get("from").getAsInt();
        }

        trigger.checkTo = false;
        if (data.get("to") != null) {
            trigger.checkTo = true;
            trigger.to = data.get("to").getAsInt();
        }

        return trigger;
    }

    @Override
    public void serialize(JsonObject data) {
        if (amount != 1) {
            data.addProperty("amount", amount);
        }

        if (checkFrom) {
            data.addProperty("from", from);
        }

        if (checkTo) {
            data.addProperty("to", to);
        }
    }

    @Override
    protected boolean canIncrease(Object... data) {
        int fromDim = asInt(data);
        int toDim = asInt(data, 1);
        if (checkFrom) {
            if (from != fromDim) return false;
        }

        if (checkTo) {
            if (to != toDim) return false;
        }

        return true;
    }

    @Override
    public Result clicked() {
        if (mouseX <= 94 && mouseX >= 1) {
            if (mouseY >= 17 && mouseY <= 24) amountEdit.select();
            if (mouseY >= 25 && mouseY <= 33) checkFrom = !checkFrom;
            if (mouseY >= 34 && mouseY <= 41) fromEdit.select();
            if (mouseY >= 42 && mouseY <= 50) checkTo = !checkTo;
            if (mouseY >= 51 && mouseY <= 59) toEdit.select();
            if (mouseY >= 17 && mouseY <= 59) return Result.ALLOW;
        }
        
        return Result.DEFAULT;
    }

    @Override
    public void draw() {
        int colorEdit = theme.optionsFontColor;
        int colorCheckFrom = theme.optionsFontColor;
        int colorFromEdit = theme.optionsFontColor;
        int colorCheckTo = theme.optionsFontColor;
        int colorToEdit = theme.optionsFontColor;
        if (ClientHelper.canEdit()) {
            if (mouseX <= 94 && mouseX >= 1) {
                if (mouseY >= 17 && mouseY <= 24) colorEdit = theme.optionsFontColorHover;
                if (mouseY >= 25 && mouseY <= 33) colorCheckFrom = theme.optionsFontColorHover;
                if (mouseY >= 34 && mouseY <= 41) colorFromEdit = theme.optionsFontColorHover;
                if (mouseY >= 42 && mouseY <= 50) colorCheckTo = theme.optionsFontColorHover;
                if (mouseY >= 51 && mouseY <= 59) colorToEdit = theme.optionsFontColorHover;
            }
        }

        drawText("times: " + amountEdit, 4, 18, colorEdit);
        drawText("checkFrom: " + checkFrom, 4, 26, colorCheckFrom);
        drawText("from: " + fromEdit, 4, 34, colorFromEdit);
        drawText("checkTo: " + checkTo, 4, 42, colorCheckTo);
        drawText("to: " + toEdit, 4, 51, colorToEdit);
    }

    @Override
    public int getAmountRequired() {
        return amount;
    }

    @Override
    public void addTooltip(List<String> toolTip) {
        if (checkFrom && !checkTo) {
            toolTip.add("Change from Dimension " + from + ", " + amount + " times");
        } else if (checkTo && !checkFrom) {
            toolTip.add("Change to Dimension " + to + ", " + amount + " times");
        } else if (checkTo && checkFrom) {
            toolTip.add("Change to Dimension " + to + " from Dimension + " + from + " , " + amount + " times");
        }
    }
}
