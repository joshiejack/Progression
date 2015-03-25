package joshie.crafting.trigger;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ITrigger;
import joshie.crafting.plugins.minetweaker.Triggers;
import minetweaker.MineTweakerAPI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@ZenClass("mods.craftcontrol.triggers.Kill")
public class TriggerKill extends TriggerBaseCounter {
	private String entity;
	
	public TriggerKill() {
		super("Kill");
	}
	
	@SubscribeEvent
	public void onEvent(LivingDeathEvent event) {
		Entity source = event.source.getSourceOfDamage();
		if (source instanceof EntityPlayer) {
			String entity = EntityList.getEntityString(event.entity);
			CraftingAPI.registry.fireTrigger((EntityPlayer)source, getTypeName(), entity);
		}
	}
	
	@ZenMethod
	public void add(String unique, String entity, @Optional int amount) {
		TriggerKill trigger = new TriggerKill();
		trigger.entity = entity;
		if (amount <= 1) {
			trigger.amount = 1;
		} else trigger.amount = amount;

		MineTweakerAPI.apply(new Triggers(unique, trigger));
	}
	
	@Override
	public ITrigger deserialize(JsonObject data) {
		TriggerKill trigger = new TriggerKill();
		trigger.entity = data.get("Entity").getAsString();
		if (data.get("amount") != null) {
			trigger.amount = data.get("Amount").getAsInt();
		}
		
		return trigger;
	}
	
	@Override
	public void serialize(JsonObject data) {
		data.addProperty("Entity", entity);
		if (amount != 1) {
			data.addProperty("Amount", amount);
		}
	}

	@Override
	protected boolean canIncrease(Object... data) {
		return asString(data).equals(entity);
	}
}
