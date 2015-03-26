package joshie.crafting.trigger;

import joshie.crafting.api.Bus;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ITrigger;
import joshie.crafting.api.ITriggerData;
import joshie.crafting.helpers.StackHelper;
import joshie.crafting.plugins.minetweaker.MTHelper;
import joshie.crafting.plugins.minetweaker.Triggers;
import joshie.crafting.trigger.data.DataCrafting;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import net.minecraft.item.ItemStack;
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
		daytime.stack = MTHelper.getItemStack(stack);
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
		if (data.get("matchDamage") != null) {
			trigger.matchDamage = data.get("matchDamage").getAsBoolean();
		}
		
		if (data.get("matchNBT") != null) {
			trigger.matchNBT = data.get("matchNBT").getAsBoolean();
		}
		
		if (data.get("craftingTimes") != null) {
			trigger.craftingTimes = data.get("craftingTimes").getAsInt();
		}
		
		if (data.get("itemAmount") != null) {
			trigger.itemAmount = data.get("itemAmount").getAsInt();
		}
		
		return trigger;
	}

	@Override
	public void serialize(JsonObject data) {
		data.addProperty("Item", StackHelper.getStringFromStack(stack));
		if (matchDamage != true)
			data.addProperty("matchDamage", matchDamage);
		if (matchNBT != false)
			data.addProperty("matchNBT", matchNBT);
	}
	
	@Override
	public ITriggerData newData() {
		return new DataCrafting();
	}

	@Override
	public boolean isCompleted(ITriggerData existing) {
		DataCrafting data = (DataCrafting) existing;
		return data.amountCrafted >= itemAmount && data.timesCrafted >= craftingTimes;
	}

	@Override
	public void onFired(ITriggerData existing, Object... additional) {
		DataCrafting data = (DataCrafting) existing;
		ItemStack crafted = asStack(additional);
		
		if (stack.getItem() == crafted.getItem()) {
			if (matchDamage && stack.getItemDamage() != crafted.getItemDamage()) return;
			if (matchNBT && stack.getTagCompound() != crafted.getTagCompound()) return;
			data.amountCrafted += stack.stackSize;
			data.timesCrafted++;
		}
	}
}
