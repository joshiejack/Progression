package joshie.crafting;

import java.util.HashSet;

import joshie.crafting.api.Bus;
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
		activeTriggers.add(trigger); //Add the new trigger
		HashSet activeTriggerTypes = new HashSet();
		for (ITrigger existing: activeTriggers) { //Grab a list of all the triggers that should be registered
			activeTriggerTypes.add(existing.getTypeName());
		}
		
		for (ITriggerType type: CraftAPIRegistry.triggerTypes.values()) { //Loop through all trigger types
			if (activeTriggerTypes.contains(type.getTypeName())) { //If we haven't added this type to active triggers yet add it
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
	
	public static void onTriggerRemoved(ITrigger trigger) {
		activeTriggers.remove(trigger); //Add the new trigger
		HashSet activeTriggerTypes = new HashSet();
		for (ITrigger existing: activeTriggers) { //Grab a list of all the triggers that should be registered
			activeTriggerTypes.add(existing.getTypeName());
		}
		
		for (ITriggerType type: CraftAPIRegistry.triggerTypes.values()) { //Loop through all trigger types
			if (!activeTriggerTypes.contains(type.getTypeName())) { //If this trigger type is no longer in the active ones, unregister it
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
		}
	}
	
	public static void onRewardAdded(IReward reward) {
		activeRewards.add(reward); //Add the new reward
		HashSet activeRewardTypes = new HashSet();
		for (IReward existing: activeRewards) { //Grab a list of all the rewards that should be registered
			activeRewardTypes.add(existing.getTypeName());
		}
		
		for (IRewardType type: CraftAPIRegistry.rewardTypes.values()) { //Loop through all reward types
			if (!activeRewardTypes.contains(type.getTypeName())) { //If we haven't added this type to active rewards yet add it
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
	
	public static void onRewardRemoved(IReward reward) {
		activeRewards.remove(reward); //Add the new reward
		HashSet activeRewardTypes = new HashSet();
		for (IReward existing: activeRewards) { //Grab a list of all the rewards that should be registered
			activeRewardTypes.add(existing.getTypeName());
		}
		
		for (IRewardType type: CraftAPIRegistry.rewardTypes.values()) { //Loop through all reward types
			if (!activeRewardTypes.contains(type.getTypeName())) { //If this reward type is no longer in the active ones, unregister it
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
		}
	}
}
