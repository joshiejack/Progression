package joshie.crafting.trigger;

import joshie.crafting.api.Bus;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ITrigger;
import joshie.crafting.plugins.minetweaker.Triggers;
import minetweaker.MineTweakerAPI;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

@ZenClass("mods.craftcontrol.triggers.Login")
public class TriggerLogin extends TriggerBaseCounter {
	private int amount = 1;
	
	public TriggerLogin() {
		super("login");
	}
	
	@Override
	public Bus getBusType() {
		return Bus.FML;
	}
	
	@SubscribeEvent
	public void onEvent(PlayerLoggedInEvent event) {
		CraftingAPI.registry.fireTrigger(event.player, getTypeName());
	}
	
	@ZenMethod
	public void add(String unique, @Optional int amount) {
		TriggerLogin trigger = new TriggerLogin();
		if (amount <= 1) {
			trigger.amount = 1;
		} else trigger.amount = amount;

		MineTweakerAPI.apply(new Triggers(unique, trigger));
	}
	
	@Override
	public ITrigger deserialize(JsonObject data) {
		TriggerLogin trigger = new TriggerLogin();
		if (data.get("amount") != null) {
			trigger.amount = data.get("amount").getAsInt();
		}
		
		return trigger;
	}
	
	@Override
	public void serialize(JsonObject data) {
		if (amount != 1) {
			data.addProperty("amount", amount);
		}
	}
	
	@Override
	protected boolean canIncrease(Object... data) {
		return true;
	}
}
