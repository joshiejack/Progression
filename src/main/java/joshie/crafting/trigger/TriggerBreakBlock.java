package joshie.crafting.trigger;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ITrigger;
import joshie.crafting.helpers.StackHelper;
import joshie.crafting.plugins.minetweaker.MTHelper;
import joshie.crafting.plugins.minetweaker.Triggers;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.oredict.IOreDictEntry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@ZenClass("mods.craftcontrol.triggers.BreakBlock")
public class TriggerBreakBlock extends TriggerBaseCounter {
	private String oreDictionary = "NONE";
	private Block block;
	private int meta = 0;
	private boolean matchDamage = true;
	private int amount = 1;
	
	public TriggerBreakBlock() {
		super("Break Block");
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onEvent(BreakEvent event) {
		CraftingAPI.registry.fireTrigger(event.getPlayer(), getTypeName(), event.block, event.blockMetadata);
	}
	
	@ZenMethod
	public void add(String unique, IItemStack item, @Optional int amount, @Optional boolean ignoreDamage) {
		TriggerBreakBlock trigger = new TriggerBreakBlock();
		ItemStack iStack = MTHelper.getItemStack(item);
		Block block = Block.getBlockFromItem(iStack.getItem());
		trigger.block = block;
		trigger.meta = Math.max(15, iStack.getItemDamage());
		if (amount <= 1) {
			trigger.amount = 1;
		} else trigger.amount = amount;
		
		if (ignoreDamage) {
			trigger.matchDamage = false;
		}

		MineTweakerAPI.apply(new Triggers(unique, trigger));
	}
	
	@ZenMethod
	public void add(String unique, IOreDictEntry item, @Optional int amount) {
		TriggerBreakBlock trigger = new TriggerBreakBlock();
		trigger.oreDictionary = item.getName();
		if (amount <= 1) {
			trigger.amount = 1;
		} else trigger.amount = amount;

		MineTweakerAPI.apply(new Triggers(unique, trigger));
	}
	
	@Override
	public ITrigger deserialize(JsonObject data) {
		TriggerBreakBlock trigger = new TriggerBreakBlock();
		if (data.get("Ore") != null) {
			trigger.oreDictionary = data.get("Ore").getAsString();
		} else {
			String stack = data.get("Item").getAsString();
			ItemStack iStack = StackHelper.getStackFromString(stack);
			trigger.block = Block.getBlockFromItem(iStack.getItem());
			trigger.meta = iStack.getItemDamage();
			if (data.get("Match Damage") != null) {
				trigger.matchDamage = data.get("Match Damage").getAsBoolean();
			}
		}
		
		if (data.get("Amount") != null) {
			trigger.amount = data.get("Amount").getAsInt();
		}
		
		return trigger;
	}
	
	@Override
	public void serialize(JsonObject data) {
		if (!oreDictionary.equals("NONE")) {
			data.addProperty("Ore", oreDictionary);
		} else {
			ItemStack stack = new ItemStack(block, 1, meta);			
			String serial = StackHelper.getStringFromStack(stack);
			data.addProperty("Item", serial);
			if (matchDamage != true) {
				data.addProperty("Match Damage", false);
			}
		}
		
		if (amount != 1) {
			data.addProperty("Amount", amount);
		}
	}
	
	@Override
	protected boolean canIncrease(Object... data) {		
		Block theBlock = asBlock(data, 0);
		int theMeta = asInt(data, 1);
		boolean doesMatch = false;
		if (!oreDictionary.equals("NONE")) {
			ItemStack stack = new ItemStack(theBlock, 1, theMeta);
			int[] ids = OreDictionary.getOreIDs(stack);
			for (int i: ids) {
				String oreName = OreDictionary.getOreName(i);
				if (oreName.equals(oreDictionary)) {
					doesMatch = true;
					break;
				}
			}
		} else {
			if (theBlock == block) {
				if (!matchDamage || meta == theMeta) {
					doesMatch = true;
				}
			}
		}
		
		return doesMatch;
	}
}
