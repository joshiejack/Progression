package joshie.crafting;

import java.util.HashSet;

import joshie.crafting.api.Bus;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.IReward;
import joshie.crafting.api.IRewardType;
import joshie.crafting.api.ITrigger;
import joshie.crafting.api.ITriggerType;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;

public class CraftingEventsManager {
	private static HashSet<ITrigger> activeTriggers = new HashSet();
	private static HashSet<IReward> activeRewards = new HashSet();
	
	public static void onTriggerAdded(ITrigger trigger) {
		activeTriggers.add(trigger);
		onTriggerChanged(trigger);
	}
	
	public static void onTriggerRemoved(ITrigger trigger) {
		activeTriggers.remove(trigger);
		onTriggerChanged(trigger);
	}
	
	public static void onTriggerChanged(ITrigger trigger) {
		HashSet<String> triggerTypes = new HashSet();
		for (ITrigger existing: activeTriggers) {
			triggerTypes.add(existing.getTypeName());
		}
		
		//Remove all trigger types from the event bus registry
		for (ITriggerType type: CraftingAPI.registry.getTriggerTypes()) {
			Bus bus = type.getBusType();
			if (bus == Bus.FML) {
				FMLCommonHandler.instance().bus().unregister(type);
			} else if (bus == Bus.FORGE) {
				MinecraftForge.EVENT_BUS.unregister(type);
			} else if (bus == Bus.ORE) {
				MinecraftForge.ORE_GEN_BUS.unregister(type);
			} else if (bus == Bus.TERRAIN) {
				MinecraftForge.TERRAIN_GEN_BUS.unregister(type);
			}
		}
		
		for (ITriggerType type: CraftingAPI.registry.getTriggerTypes()) {
			if (triggerTypes.contains(type.getTypeName())) {
				Bus bus = type.getBusType();
				if (bus == Bus.FML) {
					FMLCommonHandler.instance().bus().register(type);
				} else if (bus == Bus.FORGE) {
					MinecraftForge.EVENT_BUS.register(type);
				} else if (bus == Bus.ORE) {
					MinecraftForge.ORE_GEN_BUS.register(type);
				} else if (bus == Bus.TERRAIN) {
					MinecraftForge.TERRAIN_GEN_BUS.register(type);
				}
			}
		}
	}
	
	public static void onRewardAdded(IReward reward) {
		activeRewards.add(reward);
		onRewardChanged(reward);
	}
	
	public static void onRewardRemoved(IReward reward) {
		activeRewards.remove(reward);
		onRewardChanged(reward);
	}
	
	public static void onRewardChanged(IReward reward) {
		HashSet<String> rewardTypes = new HashSet();
		for (IReward existing: activeRewards) {
			rewardTypes.add(existing.getTypeName());
		}
		
		//Remove all trigger types from the event bus registry
		for (IRewardType type: CraftingAPI.registry.getRewardTypes()) {
			Bus bus = type.getBusType();
			if (bus == Bus.FML) {
				FMLCommonHandler.instance().bus().unregister(type);
			} else if (bus == Bus.FORGE) {
				MinecraftForge.EVENT_BUS.unregister(type);
			} else if (bus == Bus.ORE) {
				MinecraftForge.ORE_GEN_BUS.unregister(type);
			} else if (bus == Bus.TERRAIN) {
				MinecraftForge.TERRAIN_GEN_BUS.unregister(type);
			}
		}
		
		for (IRewardType type: CraftingAPI.registry.getRewardTypes()) {
			if (rewardTypes.contains(type.getTypeName())) {
				Bus bus = type.getBusType();
				if (bus == Bus.FML) {
					FMLCommonHandler.instance().bus().register(type);
				} else if (bus == Bus.FORGE) {
					MinecraftForge.EVENT_BUS.register(type);
				} else if (bus == Bus.ORE) {
					MinecraftForge.ORE_GEN_BUS.register(type);
				} else if (bus == Bus.TERRAIN) {
					MinecraftForge.TERRAIN_GEN_BUS.register(type);
				}
			}
		}
	}
}
