package joshie.crafting.trigger;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ITrigger;
import joshie.crafting.minetweaker.Triggers;
import minetweaker.MineTweakerAPI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@ZenClass("mods.craftcontrol.triggers.Kill")
public class TriggerKill extends TriggerBase {
	private String entity;
	private int amount = 1;
	
	public TriggerKill() {
		super("kill");
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
	public boolean isCompleted(Object[] existing) {
		int count = (Integer) existing[0];
		return count >= amount;
	}

	@Override
	public Object[] onFired(Object[] existing, Object... data) {	
		int count = asInt(existing);
		String name = asString(data);

		if (name.equals(entity)) {
			count++;
		}
		
		return new Object[] { count };
	}

	@Override
	public Object[] readFromNBT(NBTTagCompound tag) {
		return new Object[] { tag.getInteger("Count") };
	}

	@Override
	public void writeToNBT(NBTTagCompound tag, Object[] existing) {
		int count = asInt(existing);
		tag.setInteger("Count", count);
	}
}
