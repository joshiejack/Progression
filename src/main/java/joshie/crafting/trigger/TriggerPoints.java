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

@ZenClass("mods.craftcontrol.triggers.Points")
public class TriggerPoints extends TriggerBaseBoolean {
    private boolean consume = true;
    private String name;
    
	public TriggerPoints() {
		super("points");
	}
	
	@Override
	public Bus getBusType() {
		return Bus.NONE;
	}
	
	@ZenMethod
	public void add(String unique, String name, @Optional int amount, @Optional boolean keep) {
		TriggerPoints trigger = new TriggerPoints();
		trigger.name = name;
		trigger.consume = !keep;
		if (amount <= 1) {
			trigger.amount = 1;
		} else trigger.amount = amount;

		MineTweakerAPI.apply(new Triggers(unique, trigger));
	}
	
	@Override
	public ITrigger deserialize(JsonObject data) {
		TriggerPoints trigger = new TriggerPoints();
		if (data.get("consume") != null) {
		    trigger.consume = data.get("consume").getAsBoolean();
		}
		
		trigger.name = data.get("name").getAsString();
		if (data.get("amount") != null) {
			trigger.amount = data.get("amount").getAsInt();
		}
		
		return trigger;
	}
	
	@Override
	public void serialize(JsonObject data) {
	    if (consume != true) {
	        data.addProperty("consume", consume);
	    }
	    
	    data.addProperty("name", name);
		if (amount != 1) {
			data.addProperty("amount", amount);
		}
	}
	
	@Override
    public void onFired(UUID uuid, ITriggerData iTriggerData, Object... data) {    
	    int total = CraftingAPI.players.getPlayerData(uuid).getAbilities().getPoints(name);
	    if (total >= amount) {
            ((DataBoolean)iTriggerData).completed = true;
            if (consume) {
                CraftingAPI.players.getServerPlayer(uuid).addPoints(name, -amount);
            }
        }
    }
}
