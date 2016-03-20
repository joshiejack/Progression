package joshie.progression.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

import joshie.progression.api.ICriteria;
import joshie.progression.api.IRewardType;
import joshie.progression.api.IStoreNBTData;
import joshie.progression.api.ITab;
import joshie.progression.handlers.APIHandler;
import joshie.progression.handlers.RemappingHandler;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.player.PlayerTeam.TeamType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.UsernameCache;

public class PlayerSavedData extends WorldSavedData {
    private HashMap<UUID, PlayerTeam> teams = Maps.newHashMap();
    private HashMap<PlayerTeam, PlayerDataServer> data = Maps.newHashMap();

    public PlayerSavedData(String data) {
        super(data);
    }

    public Collection<PlayerDataServer> getPlayerData() {
        return data.values();
    }

    public boolean reset(String username) {
        //Search the active players first for a match
        UUID uuid = null;
        for (EntityPlayerMP player : PlayerHelper.getAllPlayers()) {
            if (player.getGameProfile().getName().equalsIgnoreCase(username)) {
                uuid = PlayerHelper.getUUIDForPlayer(player);
                break;
            }
        }

        //We've searched online players, now we search the username cache instead
        for (Map.Entry<UUID, String> entry : UsernameCache.getMap().entrySet()) {
            String name = entry.getValue();
            if (name.equals(username)) {
                uuid = entry.getKey();
                break;
            }
        }

        //If we found then reset the data
        if (uuid != null) {
            PlayerTeam team = teams.get(uuid);
            if (team != null) {
                teams.remove(uuid); //Remove this uuid from the cache
                data.remove(team); //Remove this team from the cache

                List<EntityPlayerMP> list = new ArrayList();
                //Now that we have removed everything we should resync the data to the players in question
                for (EntityPlayerMP player : PlayerHelper.getAllPlayers()) {
                    if (PlayerHelper.getUUIDForPlayer(player).equals(team.getOwner())) {
                        list.add((EntityPlayerMP) player);
                    }

                    for (UUID member : team.getMembers()) {
                        if (PlayerHelper.getUUIDForPlayer(player).equals(member)) {
                            list.add((EntityPlayerMP) player);
                        }
                    }
                }

                //Now that we have a list of all online players, let them know!
                for (EntityPlayerMP player : list) {
                    RemappingHandler.onPlayerConnect(player);
                }

                return true;
            }
        }

        return false;
    }

    public PlayerDataServer getServerPlayer(UUID uuid) {
        PlayerTeam team = teams.get(uuid);
        if (team == null) {
            //IF this player couldn't be found via this uiid
            //If this UUID was not found, Search the username cache for this players username
            //Once we've found it, then use the cache value as the uuid instead
            EntityPlayer player = PlayerHelper.getPlayerFromUUID(uuid);
            if (player != null) {
                String name = player.getGameProfile().getName();
                for (Map.Entry<UUID, String> entry : UsernameCache.getMap().entrySet()) {
                    if (entry.getValue().equals(name)) {
                        uuid = entry.getKey();
                        team = teams.get(uuid);
                        break;
                    }
                }
            }

            //If all else fails create a new one
            if (team == null) {
                team = new PlayerTeam(TeamType.SINGLE, uuid);
                teams.put(uuid, team);
            }
        }

        PlayerDataServer data = this.data.get(team);
        if (data == null) {
            data = new PlayerDataServer(team);
            this.data.put(team, data);
            this.markDirty();
            //If this team never existed before
            //Loop through all the rewards loaded and init them with player data
            for (ITab tab : APIHandler.getTabs().values()) {
                for (ICriteria criteria : tab.getCriteria()) {
                    for (IRewardType reward : criteria.getRewards()) {
                        if (reward instanceof IStoreNBTData) {
                            IStoreNBTData storage = (IStoreNBTData) reward;
                            data.getCustomStats().setCustomData(storage.getNBTKey(), storage.getDefaultTags(new NBTTagCompound()));
                        }
                    }
                }
            }
        }

        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        /** Load in the data about teams **/
        NBTTagList map = nbt.getTagList("PlayerData", 10);
        for (int i = 0; i < map.tagCount(); i++) {
            NBTTagCompound tag = map.getCompoundTagAt(i);
            if (tag.hasKey("Owner") || tag.hasKey("UUID-Most")) {
                PlayerTeam team = new PlayerTeam();
                team.readFromNBT(tag);
                PlayerDataServer server = new PlayerDataServer(team);
                server.readFromNBT(tag);
                data.put(team, server);
            }
        }

        /** Create the mappings for team member > team **/
        for (PlayerTeam team : data.keySet()) {
            if (team.isActive()) {
                teams.put(team.getOwner(), team);
                for (UUID member : team.getMembers()) {
                    teams.put(member, team);
                }
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagList map = new NBTTagList();
        for (PlayerTeam team : data.keySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            team.writeToNBT(tag);
            data.get(team).writeToNBT(tag);
            map.appendTag(tag);
        }

        nbt.setTag("PlayerData", map);
    }
}
