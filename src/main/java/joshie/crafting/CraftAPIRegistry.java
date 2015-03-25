package joshie.crafting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICondition;
import joshie.crafting.api.IConditionType;
import joshie.crafting.api.ICraftingMappings;
import joshie.crafting.api.ICriteria;
import joshie.crafting.api.IRegistry;
import joshie.crafting.api.IReward;
import joshie.crafting.api.IRewardType;
import joshie.crafting.api.ITrigger;
import joshie.crafting.api.ITriggerType;
import joshie.crafting.helpers.PlayerHelper;
import joshie.crafting.json.JSONLoader;
import joshie.crafting.player.PlayerDataServer;
import joshie.crafting.trigger.TriggerResearch;
import minetweaker.MineTweakerAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class CraftAPIRegistry implements IRegistry {
	//This is the registry for trigger type and reward type creation
	private final HashMap<String, ITriggerType> triggerTypes = new HashMap();
	private final HashMap<String, IRewardType> rewardTypes = new HashMap();
	private final HashMap<String, IConditionType> conditionTypes = new HashMap();
	
	//These four maps are registries for fetching the various types
	private HashMap<String, ITrigger> triggers = new HashMap();
	private HashMap<String, IReward> rewards = new HashMap();
	private HashMap<String, ICriteria> criteria = new HashMap();
	private HashMap<String, ICondition> conditions = new HashMap();
	
	@Override
	public void loadMineTweaker3() {
		for (ITriggerType type: triggerTypes.values()) {
			MineTweakerAPI.registerClass(type.getClass());
		}
		
		for (IRewardType type: rewardTypes.values()) {
			MineTweakerAPI.registerClass(type.getClass());
		}
		
		for (IConditionType type: conditionTypes.values()) {
			MineTweakerAPI.registerClass(type.getClass());
		}
		
		MineTweakerAPI.registerClass(CraftingCriteria.class);
	}
	
	@Override
	public void resyncPlayers() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			serverRemap(); //Remap all the data for the server
			//Send the new mappings to all the client
			for (EntityPlayer player: PlayerHelper.getAllPlayers()) {
				UUID uuid = PlayerHelper.getUUIDForPlayer(player);
				CraftingAPI.players.getPlayerData(uuid).getMappings().remap();
				CraftingAPI.players.getServerPlayer(uuid).getMappings().syncToClient((EntityPlayerMP)player);
			} //Resync the new data to the client
		}
	}
	
	@Override
	public void resetData() {
		//Resets the data for the players and the client
		//Clear out all of the data for the players
		CraftingMod.instance.createWorldData();
		resyncPlayers();
	}

	@Override
	public void reloadJson() {
		triggers = new HashMap();
		rewards = new HashMap();
		criteria = new HashMap();
		conditions = new HashMap();
		JSONLoader.loadJSON();
		resyncPlayers();
	}
	
	@Override
	public Collection<ICriteria> getCriteria() {
		return criteria.values();
	}
	
	@Override //Fired Server Side only
	public boolean fireTrigger(UUID uuid, String string, Object... data) {
		return CraftingAPI.players.getServerPlayer(uuid).getMappings().fireAllTriggers(string, data);
	}
	
	@Override //Fired Server Side only
	public boolean fireTrigger(EntityPlayer player, String string, Object... data) {
		if (!player.worldObj.isRemote) {
			return fireTrigger(PlayerHelper.getUUIDForPlayer(player), string, data);
		} else return false;
	}

	@Override
	public IConditionType registerConditionType(IConditionType type) {
		conditionTypes.put(type.getTypeName(), type);
		return type;
	}
	
	@Override
	public ITriggerType registerTriggerType(ITriggerType type) {
		triggerTypes.put(type.getTypeName(), type);
		return type;
	}
	
	@Override
	public IRewardType registerRewardType(IRewardType type) {
		rewardTypes.put(type.getTypeName(), type);
		return type;
	}

	@Override
	public ICriteria newCriteria(String name) {
		ICriteria condition = new CraftingCriteria().setUniqueName(name);
		criteria.put(name, condition);
		return condition;
	}

	@Override
	public void removeCondition(String unique) {
		conditions.remove(unique);
	}

	@Override
	public void removeTrigger(String unique) {
		ITrigger trigger = triggers.get(unique);
		CraftingEventsManager.onTriggerRemoved(trigger);
		triggers.remove(unique);
	}

	@Override
	public void removeReward(String unique) {
		IReward reward = rewards.get(unique);
		CraftingEventsManager.onRewardRemoved(reward);
		rewards.remove(unique);
	}

	@Override
	public void removeCriteria(String unique) {
		criteria.remove(unique);
	}

	@Override
	public ICondition getCondition(String name, String unique, JsonObject data) {
		ICondition condition = conditions.get(unique);
		if (condition == null && name != null && data != null) {
			condition = (ICondition) conditionTypes.get(name).deserialize(data).setUniqueName(unique);
			conditions.put(unique, condition);
		}
		
		return condition;
	}
	
	@Override
	public ITrigger getTrigger(String name, String unique, JsonObject data) {
		ITrigger trigger = triggers.get(unique);
		if (trigger == null && name != null && data != null) {
			//New trigger created
			trigger = (ITrigger) triggerTypes.get(name).deserialize(data).setUniqueName(unique);
			CraftingEventsManager.onTriggerAdded(trigger);
			triggers.put(unique, trigger);
		}
		
		return trigger;
	}
	
	@Override
	public IReward getReward(String name, String unique, JsonObject data) {
		IReward reward = rewards.get(unique);
		if (reward == null && name != null && data != null) {
			reward = (IReward) rewardTypes.get(name).deserialize(data).setUniqueName(unique);
			CraftingEventsManager.onRewardAdded(reward);
			rewards.put(unique, reward);
		}
		
		return reward;
	}

	@Override
	public ICriteria getCriteriaFromName(String name) {
		return criteria.get(name);
	}
	
	private List technologies;

	@Override
	public List<ITrigger> getTechnology() {
		if (technologies != null) return technologies;
		else {
			technologies = new ArrayList();
			for (String name: triggers.keySet()) {
				ITrigger research = triggers.get(name);
				if (research instanceof TriggerResearch) {
					technologies.add(research);
				}
			}
			
			return technologies;
		}
	}
	
	protected Multimap<ITrigger, ICriteria> triggerToCriteria = HashMultimap.create(); //A list of triggers to the criteria that it is relevant to
	protected Multimap<ICriteria, ICriteria> criteriaToUnlocks = HashMultimap.create(); //A list of the critera completing this one unlocks

	@Override
	public Multimap<ITrigger, ICriteria> getTriggerToCriteria() {
		return triggerToCriteria;
	}

	@Override
	public Collection<ICriteria> getCriteriaUnlocks(ICriteria criteria) {
		return criteriaToUnlocks.get(criteria);
	}

	/** Returns true if any of the passed in list, is completed **/
	private boolean containsAny(List<ICriteria> list, Set<ICriteria> completedCriteria) {
		for (ICriteria criteria: completedCriteria) {
			if (completedCriteria.contains(criteria)) return true;
		}
		
		return false;
	}
	
	@Override
	public void serverRemap() {
		Collection<ICriteria> allCriteria = criteria.values();
		//Now that we have all of the criteria that been fulfilled, we need to pass through
		Collection<PlayerDataServer> data = CraftingMod.data.getPlayerData();
		for (PlayerDataServer player: data) {
			player.getMappings().remap();
			
			ICraftingMappings mappings = player.getMappings();
			for (ICriteria criteria: allCriteria) {
				//We do not give a damn about whether this is available or not
				//The unlocking of criteria should happen no matter what
				List<ICriteria> requirements = criteria.getRequirements();
				for (ICriteria require: requirements) {
					criteriaToUnlocks.put(require, criteria);
				}
				
				//Map all triggers to the criteria
				for (ITrigger trigger: criteria.getTriggers()) {
					triggerToCriteria.put(trigger, criteria);
				}
			}
		}
	}

	@Override
	public Collection<ITriggerType> getTriggerTypes() {
		return triggerTypes.values();
	}

	@Override
	public Collection<IRewardType> getRewardTypes() {
		return rewardTypes.values();
	}
}
