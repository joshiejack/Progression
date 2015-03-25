package joshie.crafting.trigger;

import joshie.crafting.api.Bus;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ITrigger;
import joshie.crafting.helpers.StackHelper;
import joshie.crafting.minetweaker.Triggers;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

@ZenClass("mods.craftcontrol.triggers.Crafting")
public class TriggerCrafting extends TriggerBase {
	private ItemStack stack;
	private boolean matchDamage = true;
	private boolean matchNBT = false;
	private int craftingTimes = 1;
	private int itemAmount = 1;
	
	public TriggerCrafting() {
		super("crafting");
	}

	@Override
	public Bus getBusType() {
		return Bus.FML;
	}
	
	@SubscribeEvent
	public void onEvent(ItemCraftedEvent event) {
		CraftingAPI.registry.fireTrigger(event.player, getTypeName(), event.crafting.copy());
	}
	
	@ZenMethod
	public void add(String unique, IIngredient stack, @Optional boolean matchNBT, @Optional int craftTimes, @Optional int itemAmount) {
		TriggerCrafting daytime = new TriggerCrafting();
		daytime.stack = MineTweakerMC.getItemStack(stack); 
		if (daytime.stack.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
			daytime.matchDamage = false;
		}
		
		daytime.matchNBT = matchNBT;
		if (craftTimes <= 1) {
			daytime.craftingTimes = 1;
		} else daytime.craftingTimes = craftTimes;
		
		if (itemAmount <= 1) {
			daytime.itemAmount = 1;
		} else daytime.itemAmount = itemAmount;
		
		MineTweakerAPI.apply(new Triggers(unique, daytime));
	}
	
	@Override
	public ITrigger deserialize(JsonObject data) {
		TriggerCrafting trigger = new TriggerCrafting();
		trigger.stack = StackHelper.getStackFromString(data.get("Item").getAsString());
		if (data.get("Match Damage") != null) {
			trigger.matchDamage = data.get("Match Damage").getAsBoolean();
		}
		
		if (data.get("Match NBT") != null) {
			trigger.matchNBT = data.get("Match NBT").getAsBoolean();
		}
		
		if (data.get("Crafting Times") != null) {
			trigger.craftingTimes = data.get("Crafting Times").getAsInt();
		}
		
		if (data.get("Item Amount") != null) {
			trigger.itemAmount = data.get("Item Amount").getAsInt();
		}
		
		return trigger;
	}

	@Override
	public void serialize(JsonObject data) {
		data.addProperty("Item", StackHelper.getStringFromStack(stack));
		if (matchDamage != true)
			data.addProperty("Match Damage", matchDamage);
		if (matchNBT != false)
			data.addProperty("Match NBT", matchNBT);
	}

	@Override
	public boolean isCompleted(Object[] existing) {
		int amountCrafted = asInt(existing);
		int timesCrafted = asInt(existing, 1);
		return amountCrafted >= itemAmount && timesCrafted >= craftingTimes;
	}

	@Override
	public Object[] onFired(Object[] existing, Object... data) {
		ItemStack crafted = asStack(data);
		int amountCrafted = asInt(existing);
		int timesCrafted = asInt(existing, 1);
		
		if (stack.getItem() == crafted.getItem()) {
			if (matchDamage && stack.getItemDamage() != crafted.getItemDamage()) return new Object[] { amountCrafted, timesCrafted };
			if (matchNBT && stack.getTagCompound() != crafted.getTagCompound()) return new Object[] { amountCrafted, timesCrafted };
			amountCrafted += stack.stackSize;
			timesCrafted++;
		}
		
		
		return new Object[] { amountCrafted, timesCrafted };
	}

	@Override
	public Object[] readFromNBT(NBTTagCompound tag) {
		Object[] data = new Object[2];
		data[0] = tag.getInteger("AmountCrafted");
		data[1] = tag.getInteger("TimesCrafted");
		return data;
	}

	@Override
	public void writeToNBT(NBTTagCompound tag, Object[] existing) {
		int amountCrafted = (Integer) existing[0];
		int timesCrafted = (Integer) existing[1];
		tag.setInteger("AmountCrafted", amountCrafted);
		tag.setInteger("TimesCrafted", timesCrafted);
	}
}
