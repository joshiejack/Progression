package joshie.crafting;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICraftingMappings;
import joshie.crafting.api.ICriteria;
import joshie.crafting.api.ITrigger;
import joshie.crafting.helpers.PlayerHelper;
import joshie.crafting.json.JSONLoader;
import joshie.crafting.player.PlayerDataServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class CraftingRemapper {
	public static Multimap<ITrigger, ICriteria> triggerToCriteria = HashMultimap.create(); //A list of triggers to the criteria that it is relevant to
	public static Multimap<ICriteria, ICriteria> criteriaToUnlocks = HashMultimap.create(); //A list of the critera completing this one unlocks
	
	public static void resyncPlayers() {
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
	
	public static void resetData() {
		//Resets the data for the players and the client
		//Clear out all of the data for the players
		CraftingMod.instance.createWorldData();
		resyncPlayers();
	}

	public static void reloadJson() {
		CraftAPIRegistry.triggers = new HashMap();
		CraftAPIRegistry.rewards = new HashMap();
		CraftAPIRegistry.criteria = new HashMap();
		CraftAPIRegistry.conditions = new HashMap();
		JSONLoader.loadJSON();
		resyncPlayers();
	}
	
	public static void serverRemap() {
		Collection<ICriteria> allCriteria = CraftAPIRegistry.criteria.values();
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
}
