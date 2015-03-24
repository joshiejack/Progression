package joshie.crafting.trigger;

import joshie.crafting.api.ITrigger;
import joshie.crafting.minetweaker.Triggers;
import minetweaker.MineTweakerAPI;
import net.minecraft.nbt.NBTTagCompound;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.google.gson.JsonObject;

@ZenClass("mods.craftcontrol.triggers.Login")
public class TriggerLogin extends TriggerBase {
	private int amount = 1;
	
	public TriggerLogin() {
		super("login");
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
			trigger.amount = data.get("Amount").getAsInt();
		}
		
		return trigger;
	}
	
	@Override
	public void serialize(JsonObject data) {
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
		count++;
		
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
