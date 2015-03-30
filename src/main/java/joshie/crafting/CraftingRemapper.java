package joshie.crafting;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICriteria;
import joshie.crafting.api.crafting.CraftingEvent.CraftingType;
import joshie.crafting.crafting.CraftingRegistry;
import joshie.crafting.helpers.PlayerHelper;
import joshie.crafting.json.JSONLoader;
import joshie.crafting.lib.SafeStack;
import joshie.crafting.network.PacketHandler;
import joshie.crafting.network.PacketSyncJSON;
import joshie.crafting.player.PlayerDataServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class CraftingRemapper {
    public static Multimap<ICriteria, ICriteria> criteriaToUnlocks; //A list of the critera completing this one unlocks
    
    //Grabs the json, as a split string and rebuilds it, sends it in parts to the client
    public static void onPlayerConnect(EntityPlayerMP player) {        
        //Remap the player data, for this player, before doing anything else, as the data may not existing yet
        CraftingAPI.players.getPlayerData(player).getMappings().remap();
        
        if (CraftingMod.options.sync) {
            PacketHandler.sendToClient(new PacketSyncJSON(JSONLoader.serverJsonData.length), player);
        } else {
            UUID uuid = PlayerHelper.getUUIDForPlayer(player);
            //Sends all the data to do with this player to the client, so it's up to date
            CraftingAPI.players.getPlayerData(uuid).getMappings().syncToClient(player);
        }
    }

    public static void reloadServerData() {
        //Reset the data
        resetRegistries();
        
        //Now that we have reset all the data, let's reset all the players data but don't affect their completed
        Collection<PlayerDataServer> data = CraftingMod.data.getPlayerData();
        for (PlayerDataServer player: data) {
            player.getMappings().remap(); //Remapping player data, means removing everything except the completed
        }
        
        //All data has officially been wiped SERVERSIDE
        //Reload in all the data from json
        /** Grab yourself some gson, load it in from the file serverside **/        
        JSONLoader.loadServerJSON(); //This fills out all the data once again
        //Now we need to sync the new data up with all the clients
        //Grab all the online players and send them an update
        //Sends a packet to all the clients, informing them about their new data
        for (EntityPlayer player : PlayerHelper.getAllPlayers()) {
            PacketHandler.sendToClient(new PacketSyncJSON(JSONLoader.serverJsonData.length), (EntityPlayerMP) player); //Resend all the data to a client from when they connect
        }
        
        //Now that mappings have been synced to the client reload the unlocks list
        Collection<ICriteria> allCriteria = CraftAPIRegistry.criteria.values();
        for (ICriteria criteria : allCriteria) { //Remap criteria to unlocks
            //We do not give a damn about whether this is available or not
            //The unlocking of criteria should happen no matter what
            List<ICriteria> requirements = criteria.getRequirements();
            for (ICriteria require : requirements) {
                criteriaToUnlocks.put(require, criteria);
            }
        }
    }
    
    public static void resetRegistries() {
        //Resets all of the registries to default empty data
        //Create a a new unlocker
        criteriaToUnlocks = HashMultimap.create(); //Reset all data
        CraftAPIRegistry.criteria = new HashMap(); //Reset all data
        CraftAPIRegistry.conditions = new HashSet(); //Reset all data
        CraftAPIRegistry.triggers = new HashSet(); //Reset all data
        CraftAPIRegistry.rewards = new HashSet(); //Rest all data
        CraftingEventsManager.activeRewards = new HashSet(); //Reset active rewards
        CraftingEventsManager.activeTriggers = new HashSet(); //Reset active triggers
        CraftingRegistry.conditions = new HashMap(); //Reset all the data in the crafting registry
        CraftingRegistry.usage = new HashMap(); //Reset all the data in the crafting registry
        for (CraftingType type : CraftingType.craftingTypes) {
            Multimap<SafeStack, ICriteria> conditions = HashMultimap.create();
            Multimap<SafeStack, ICriteria> usage = HashMultimap.create();
            CraftingRegistry.conditions.put(type, conditions);
            CraftingRegistry.usage.put(type, usage);
        }
    }
}
