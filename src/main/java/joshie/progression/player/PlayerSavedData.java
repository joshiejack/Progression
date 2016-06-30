package joshie.progression.player;

import com.google.common.collect.Maps;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.IRewardProvider;
import joshie.progression.api.criteria.ITab;
import joshie.progression.api.special.IStoreNBTData;
import joshie.progression.handlers.APICache;
import joshie.progression.handlers.RemappingHandler;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.player.PlayerTeam.TeamType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.relauncher.Side;

import java.util.*;

import static joshie.progression.player.PlayerSavedData.TeamAction.LEAVE;
import static joshie.progression.player.PlayerSavedData.TeamAction.NEW;

public class PlayerSavedData extends WorldSavedData {
    private HashMap<UUID, PlayerTeam> teams = Maps.newHashMap();
    private HashMap<PlayerTeam, PlayerDataServer> data = Maps.newHashMap();

    public PlayerSavedData(String data) {
        super(data);
    }

    public Collection<PlayerDataServer> getPlayerData() {
        return data.values();
    }

    public void clear() {
        for (PlayerTeam team: data.keySet()) {
            PlayerDataServer data = new PlayerDataServer(team);
            this.data.put(team, data);
            this.markDirty();
            //If this team never existed before
            //Loop through all the rewards loaded and init them with player data
            for (ITab tab : APICache.getServerCache().getTabSet()) {
                for (ICriteria criteria : tab.getCriteria()) {
                    for (IRewardProvider provider : criteria.getRewards()) {
                        if (provider.getProvided() instanceof IStoreNBTData) {
                            IStoreNBTData storage = (IStoreNBTData) provider.getProvided();
                            data.getCustomStats().setCustomData(storage.getNBTKey(), storage.getDefaultTags(new NBTTagCompound()));
                        }
                    }
                }
            }
        }
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

    public enum  TeamAction {
        LEAVE, NEW, JOIN;
    }

    //Packet calls this
    public void joinTeam(EntityPlayer player, TeamAction action, UUID owner, String name) {
        UUID uuid = PlayerHelper.getUUIDForPlayer(player);
        PlayerTeam original = teams.get(uuid);
        if (!original.removeMember(uuid) && !original.isSingle()) { //If the team no longer exists and isn't a single player team, let's delete the data for it
            data.remove(original); //Remove the data
            teams.remove(original.getOwner()); //Remove the association
        } else original.syncChanges(Side.SERVER); //Sync the information about the original team to all members

        //Now that the player has left the old team, and updated all the players about such details,
        //We should add the player to the new team that they are joining
        PlayerTeam newTeam = null;
        if (action == LEAVE) { //If the player is leaving the team to go back to single player
            for (PlayerTeam team: data.keySet()) { //Let's find where we are the owner
                if (team.isSingle() && team.getOwner().equals(uuid)) {
                    newTeam = team; //If we've found our original single player team, readd it
                    break;
                }
            }
        } else if (action == NEW) { //If we are creating a new team
            newTeam = new PlayerTeam(TeamType.TEAM, name, uuid);
        } else {
            if (name != null) {
                for (PlayerTeam team: data.keySet()) {
                    if (team.getName().equals(name)) {
                        newTeam = team;
                        break;
                    }
                }

                if (newTeam == null || !newTeam.isInvited(uuid)) return; //We fail if we couldn't find
            } else newTeam = teams.get(owner);
        }

        //Now that we have our newteam selected, we need to add ourself as a member
        if (!newTeam.isSingle() && !newTeam.getOwner().equals(uuid)) {
            newTeam.addMember(uuid); //Add ourselves to this new team
        }

        //Mark the new team for this uuid, instead of the old
        teams.put(uuid, newTeam);
        //Sync the changes to all team members
        newTeam.syncChanges(Side.SERVER);
        //Force create the player data
        getServerPlayer(uuid); //We've remapped

        //Reload the data for this player about his new team, and his new statistics
        RemappingHandler.onPlayerConnect((EntityPlayerMP) player);
        //Save
        markDirty();
    }

    public PlayerDataServer getServerPlayer(UUID uuid) {
        PlayerTeam team = teams.get(uuid);
        if (team == null) {
            String name = null;
            EntityPlayer player = PlayerHelper.getPlayerFromUUID(false, uuid);
            if (player != null) {
                name = player.getGameProfile().getName();
            }

            team = new PlayerTeam(TeamType.SINGLE, name, uuid);
            teams.put(uuid, team);
        }

        PlayerDataServer data = this.data.get(team);
        if (data == null) {
            data = new PlayerDataServer(team);
            this.data.put(team, data);
            this.markDirty();
            //If this team never existed before
            //Loop through all the rewards loaded and init them with player data
            for (ITab tab : APICache.getServerCache().getTabSet()) {
                for (ICriteria criteria : tab.getCriteria()) {
                    for (IRewardProvider provider : criteria.getRewards()) {
                        if (provider.getProvided() instanceof IStoreNBTData) {
                            IStoreNBTData storage = (IStoreNBTData) provider.getProvided();
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
        NBTTagList map = nbt.getTagList("TeamData", 10);
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

        nbt.setTag("TeamData", map);
    }
}
