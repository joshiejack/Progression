package joshie.progression.handlers;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import joshie.progression.api.ICriteria;
import joshie.progression.crafting.ActionType;
import joshie.progression.crafting.CraftingRegistry;
import joshie.progression.criteria.Criteria;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.json.JSONLoader;
import joshie.progression.json.Options;
import joshie.progression.lib.SafeStack;
import joshie.progression.network.PacketHandler;
import joshie.progression.network.PacketSyncJSON;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayerMP;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class RemappingHandler {
    public static Multimap<Criteria, Criteria> criteriaToUnlocks; //A list of the critera completing this one unlocks
    
    //Grabs the json, as a split string and rebuilds it, sends it in parts to the client
    public static void onPlayerConnect(EntityPlayerMP player) {        
        //Remap the player data, for this player, before doing anything else, as the data may not existing yet
        PlayerTracker.getServerPlayer(player).getMappings().remap();
        
        if (Options.sync) {
            PacketHandler.sendToClient(new PacketSyncJSON(JSONLoader.serverTabJsonData.length), player);
        } else {
            UUID uuid = PlayerHelper.getUUIDForPlayer(player);
            //Sends all the data to do with this player to the client, so it's up to date
            PlayerTracker.getServerPlayer(uuid).getMappings().syncToClient(player);
        }
    }

    public static void reloadServerData() {
        //Reset the data
        resetRegistries();
               
        //All data has officially been wiped SERVERSIDE
        //Reload in all the data from json
        /** Grab yourself some gson, load it in from the file serverside **/        
        JSONLoader.loadServerJSON(); //This fills out all the data once again
        
        //Now that mappings have been synced to the client reload the unlocks list
        Collection<Criteria> allCriteria = APIHandler.criteria.values();
        for (Criteria criteria : allCriteria) { //Remap criteria to unlocks
            //We do not give a damn about whether this is available or not
            //The unlocking of criteria should happen no matter what
            List<Criteria> requirements = criteria.prereqs;
            for (Criteria require : requirements) {
                criteriaToUnlocks.get(require).add(criteria);
            }
        }
    }
    
    public static void resetRegistries() {
        //Resets all of the registries to default empty data
        //Create a a new unlocker
        criteriaToUnlocks = HashMultimap.create(); //Reset all data
        APIHandler.tabs = new HashMap(); //Reset all data
        APIHandler.criteria = new HashMap(); //Reset all data
        EventsManager.activeRewards = new HashSet(); //Reset active rewards
        EventsManager.activeTriggers = new HashSet(); //Reset active triggers
        CraftingRegistry.conditions = new HashMap(); //Reset all the data in the crafting registry
        CraftingRegistry.usage = new HashMap(); //Reset all the data in the crafting registry
        for (ActionType type : ActionType.values()) {
            Multimap<SafeStack, ICriteria> conditions = HashMultimap.create();
            Multimap<SafeStack, ICriteria> usage = HashMultimap.create();
            CraftingRegistry.conditions.put(type, conditions);
            CraftingRegistry.usage.put(type, usage);
        }
    }
}
