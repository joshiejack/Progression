package joshie.progression.handlers;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import joshie.progression.api.ICriteria;
import joshie.progression.crafting.ActionType;
import joshie.progression.crafting.CraftingRegistry;
import joshie.progression.criteria.Criteria;
import joshie.progression.json.JSONLoader;
import joshie.progression.lib.SafeStack;
import joshie.progression.network.PacketHandler;
import joshie.progression.network.PacketSyncJSON;
import joshie.progression.network.PacketSyncJSON.Section;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class RemappingHandler {
    public static Multimap<Criteria, Criteria> criteriaToUnlocks; //A list of the critera completing this one unlocks
    
    public static String getHostName() {
        String hostname = MinecraftServer.getServer().isDedicatedServer()? MinecraftServer.getServer().getHostname(): "ssp";  
        if (hostname.equals("")) hostname = "smp";
        return hostname;
    }

    //Grabs the json, as a split string and rebuilds it, sends it in parts to the client
    public static void onPlayerConnect(EntityPlayerMP player) {
        //Remap the player data, for this player, before doing anything else, as the data may not existing yet
        PlayerTracker.getServerPlayer(player).getMappings().remap();
        
        
        PacketHandler.sendToClient(new PacketSyncJSON(Section.SEND_HASH, JSONLoader.serverHashcode, getHostName()), player);
    }

    public static void reloadServerData() {
        //Reset the data
        resetRegistries();

        //All data has officially been wiped SERVERSIDE
        //Reload in all the data from json
        /** Grab yourself some gson, load it in from the file serverside **/
        JSONLoader.loadJSON(false, JSONLoader.getTabs()); //This fills out all the data once again

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
