package joshie.crafting.trigger;

import java.util.UUID;

import joshie.crafting.api.Bus;
import joshie.crafting.api.DrawHelper;
import joshie.crafting.api.ITriggerData;
import joshie.crafting.gui.TextFieldHelper;
import joshie.crafting.gui.TextFieldHelper.IntegerFieldHelper;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.JSONHelper;
import joshie.crafting.json.Theme;
import joshie.crafting.player.PlayerTracker;
import joshie.crafting.trigger.data.DataBoolean;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.Event.Result;

public class TriggerPoints extends TriggerBaseBoolean {
    private TextFieldHelper nameEdit;
    private IntegerFieldHelper amountEdit;
    public int amount = 1;
    public boolean consume = true;
    public String name = "research";

    public TriggerPoints() {
        super("points", 0xFFB2B200);
        nameEdit = new TextFieldHelper("name", this);
        amountEdit = new IntegerFieldHelper("amount", this);
    }

    @Override
    public Bus getEventBus() {
        return Bus.NONE;
    }

    @Override
    public void readFromJSON(JsonObject data) {
        name = JSONHelper.getString(data, "research", name);
        consume = JSONHelper.getBoolean(data, "consume", consume);
        amount = JSONHelper.getInteger(data, "amount", amount);
    }

    @Override
    public void writeToJSON(JsonObject data) {
        JSONHelper.setString(data, "research", name, "research");
        JSONHelper.setBoolean(data, "consume", consume, true);
        JSONHelper.setInteger(data, "amount", amount, 1);
    }

    @Override
    public void onFired(UUID uuid, ITriggerData iTriggerData, Object... data) {
        int total = PlayerTracker.getServerPlayer(uuid).getAbilities().getPoints(name);
        if (total >= amount) {
            ((DataBoolean) iTriggerData).completed = true;
            if (consume) {
                PlayerTracker.getServerPlayer(uuid).addPoints(name, -amount);
            }
        }
    }

    @Override
    public Result onClicked(int mouseX, int mouseY) {
        if (mouseX <= 84 && mouseX >= 1) {
            if (mouseY >= 17 && mouseY <= 25) nameEdit.select();
            if (mouseY > 25 && mouseY <= 33) amountEdit.select();
            if (mouseY > 34 && mouseY <= 41) consume = !consume;
            if (mouseY >= 17 && mouseY <= 33) return Result.ALLOW;
        }

        return Result.DEFAULT;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        int color = Theme.INSTANCE.optionsFontColor;
        int amountColor = Theme.INSTANCE.optionsFontColor;
        int consumeColor = Theme.INSTANCE.optionsFontColor;
        if (ClientHelper.canEdit()) {
            if (mouseX <= 84 && mouseX >= 1) {
                if (mouseY >= 17 && mouseY <= 25) color = Theme.INSTANCE.optionsFontColorHover;
                if (mouseY > 25 && mouseY <= 33) amountColor = Theme.INSTANCE.optionsFontColorHover;
                if (mouseY > 34 && mouseY <= 41) consumeColor = Theme.INSTANCE.optionsFontColorHover;
            }
        }

        DrawHelper.triggerDraw.drawText("name: " + nameEdit, 4, 18, color);
        DrawHelper.triggerDraw.drawText("amount: " + amountEdit, 4, 26, amountColor);
        DrawHelper.triggerDraw.drawText("consume: " + consume, 4, 34, consumeColor);
    }
}
