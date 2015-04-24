package joshie.crafting.trigger;

import joshie.crafting.api.Bus;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.DrawHelper;
import joshie.crafting.gui.TextFieldHelper.IntegerFieldHelper;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.JSONHelper;
import joshie.crafting.json.Theme;

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
        super("changeDimension", 0xFF000000);
        amountEdit = new IntegerFieldHelper("amount", this);
        fromEdit = new IntegerFieldHelper("from", this);
        toEdit = new IntegerFieldHelper("to", this);
    }

    @Override
    public Bus getEventBus() {
        return Bus.FML;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEvent(PlayerChangedDimensionEvent event) {
        CraftingAPI.registry.fireTrigger(event.player, getUnlocalisedName(), event.fromDim, event.toDim);
    }

    @Override
    public void readFromJSON(JsonObject data) {
        super.readFromJSON(data);
        checkFrom = JSONHelper.getExists(data, "from");
        from = JSONHelper.getInteger(data, "from", from);
        checkTo = JSONHelper.getExists(data, "to");
        to = JSONHelper.getInteger(data, "to", to);
    }

    @Override
    public void writeToJSON(JsonObject data) {
        super.writeToJSON(data);
        if (checkFrom) JSONHelper.setInteger(data, "from", from, -5000);
        if (checkTo) JSONHelper.setInteger(data, "to", to, -5000);
    }

    @Override
    protected boolean canIncrease(Object... data) {
        int fromDim = (Integer) data[0];
        int toDim = (Integer) data[1];
        if (checkFrom) {
            if (from != fromDim) return false;
        }

        if (checkTo) {
            if (to != toDim) return false;
        }

        return true;
    }

    @Override
    public Result onClicked(int mouseX, int mouseY) {
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
    public void draw(int mouseX, int mouseY) {
        int colorEdit = Theme.INSTANCE.optionsFontColor;
        int colorCheckFrom = Theme.INSTANCE.optionsFontColor;
        int colorFromEdit = Theme.INSTANCE.optionsFontColor;
        int colorCheckTo = Theme.INSTANCE.optionsFontColor;
        int colorToEdit = Theme.INSTANCE.optionsFontColor;
        if (ClientHelper.canEdit()) {
            if (mouseX <= 94 && mouseX >= 1) {
                if (mouseY >= 17 && mouseY <= 24) colorEdit = Theme.INSTANCE.optionsFontColorHover;
                if (mouseY >= 25 && mouseY <= 33) colorCheckFrom = Theme.INSTANCE.optionsFontColorHover;
                if (mouseY >= 34 && mouseY <= 41) colorFromEdit = Theme.INSTANCE.optionsFontColorHover;
                if (mouseY >= 42 && mouseY <= 50) colorCheckTo = Theme.INSTANCE.optionsFontColorHover;
                if (mouseY >= 51 && mouseY <= 59) colorToEdit = Theme.INSTANCE.optionsFontColorHover;
            }
        }

        DrawHelper.triggerDraw.drawText("times: " + amountEdit, 4, 18, colorEdit);
        DrawHelper.triggerDraw.drawText("checkFrom: " + checkFrom, 4, 26, colorCheckFrom);
        DrawHelper.triggerDraw.drawText("from: " + fromEdit, 4, 34, colorFromEdit);
        DrawHelper.triggerDraw.drawText("checkTo: " + checkTo, 4, 42, colorCheckTo);
        DrawHelper.triggerDraw.drawText("to: " + toEdit, 4, 51, colorToEdit);
    }
}
