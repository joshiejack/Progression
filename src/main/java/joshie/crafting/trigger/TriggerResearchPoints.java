package joshie.crafting.trigger;

import java.util.UUID;

import joshie.crafting.api.Bus;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ITrigger;
import joshie.crafting.api.ITriggerData;
import joshie.crafting.plugins.minetweaker.Triggers;
import joshie.crafting.trigger.data.DataBoolean;
import minetweaker.MineTweakerAPI;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.google.gson.JsonObject;

@ZenClass("mods.craftcontrol.triggers.Research")
public class TriggerResearchPoints extends TriggerBaseBoolean {    
    private boolean consume = true;
    
    public TriggerResearchPoints() {
        super("researchPoints");
    }
    
    @Override
    public Bus getBusType() {
        return Bus.NONE;
    }
    
    @ZenMethod
    public void add(String unique, int amount, @Optional boolean keep) {
        TriggerResearchPoints trigger = new TriggerResearchPoints();
        trigger.amount = amount;
        if (keep) {
            trigger.consume = false;
        }
        
        MineTweakerAPI.apply(new Triggers(unique, trigger));
    }
    
    @Override
    public ITrigger deserialize(JsonObject data) {
        TriggerResearchPoints trigger = new TriggerResearchPoints();
        if (data.get("amount") != null) {
            trigger.amount = data.get("amount").getAsInt();
        }
        
        if (data.get("consume") != null) {
            trigger.consume = data.get("consume").getAsBoolean();
        }
        
        return trigger;
    }

    @Override
    public void serialize(JsonObject data) {
        if (amount > 1) {
            data.addProperty("amount", amount);
        }
        
        if (consume != true) {
            data.addProperty("consume", consume);
        }
    }
    
    @Override
    public void onFired(UUID uuid, ITriggerData iTriggerData, Object... data) {    
        int total = CraftingAPI.players.getPlayerData(uuid).getAbilities().getResearchPoints();
        if (total >= amount) {
            ((DataBoolean)iTriggerData).completed = true;
            if (consume) {
                CraftingAPI.players.getServerPlayer(uuid).addResearchPoints(-amount);
            }
        }
    }
}
