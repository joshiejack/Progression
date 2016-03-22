package joshie.progression.handlers;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import joshie.progression.api.criteria.IProgressionCriteria;
import joshie.progression.crafting.CraftingRegistry;
import joshie.progression.json.DefaultSettings;
import joshie.progression.json.JSONLoader;
import joshie.progression.network.PacketHandler;
import joshie.progression.network.PacketSyncJSONToClient;
import joshie.progression.network.PacketSyncJSONToClient.Section;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class RemappingHandler {
    public static Multimap<IProgressionCriteria, IProgressionCriteria> criteriaToUnlocks; //A list of the critera completing this one unlocks
    
    public static String getHostName() {
        String hostname = FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()? FMLCommonHandler.instance().getMinecraftServerInstance().getServerHostname(): "ssp";  
        if (hostname.equals("")) hostname = "smp";
        return hostname;
    }

    //Grabs the json, as a split string and rebuilds it, sends it in parts to the client
    public static void onPlayerConnect(EntityPlayerMP player) {
        //Remap the player data, for this player, before doing anything else, as the data may not existing yet
        PlayerTracker.getServerPlayer(player).getMappings().remap();
        PacketHandler.sendToClient(new PacketSyncJSONToClient(Section.SEND_HASH, JSONLoader.serverHashcode, getHostName()), player);
    }

    public static void reloadServerData(DefaultSettings settings) {
        //Reset the data
        resetRegistries();

        //All data has officially been wiped SERVERSIDE
        //Reload in all the data from json
        /** Grab yourself some gson, load it in from the file serverside **/
        JSONLoader.loadJSON(false, settings); //This fills out all the data once again

        //Now that mappings have been synced to the client reload the unlocks list
        Collection<IProgressionCriteria> allCriteria = APIHandler.getCriteria().values();
        for (IProgressionCriteria criteria : allCriteria) { //Remap criteria to unlocks
            //We do not give a damn about whether this is available or not
            //The unlocking of criteria should happen no matter what
            List<IProgressionCriteria> requirements = criteria.getPreReqs();
            for (IProgressionCriteria require : requirements) {
                criteriaToUnlocks.get(require).add(criteria);
            }
        }
    }

    public static void resetRegistries() {
        //Resets all of the registries to default empty data
        //Create a a new unlocker
        criteriaToUnlocks = HashMultimap.create(); //Reset all data
        APIHandler.resetAPIHandler(); //Reset tabs and criteria maps
        EventsManager.create();
        CraftingRegistry.create();
    }
}
