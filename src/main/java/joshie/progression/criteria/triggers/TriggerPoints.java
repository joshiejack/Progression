package joshie.progression.criteria.triggers;

import java.util.UUID;

import joshie.progression.api.EventBusType;
import joshie.progression.api.ITriggerData;
import joshie.progression.criteria.triggers.data.DataBoolean;
import joshie.progression.gui.fields.BooleanField;
import joshie.progression.gui.fields.TextField;
import joshie.progression.helpers.JSONHelper;
import joshie.progression.player.PlayerTracker;

import com.google.gson.JsonObject;

public class TriggerPoints extends TriggerBaseBoolean {
    public String name = "research";
    public int amount = 1;
    public boolean consume = true;

    public TriggerPoints() {
        super("points", 0xFFB2B200);
        list.add(new TextField("name", this));
        list.add(new TextField("amount", this));
        list.add(new BooleanField("consume", this));
    }

    @Override
    public EventBusType getEventBus() {
        return EventBusType.NONE;
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
}
