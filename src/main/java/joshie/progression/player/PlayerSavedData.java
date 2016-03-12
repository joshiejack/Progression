package joshie.progression.player;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import com.google.common.collect.Maps;

import joshie.progression.criteria.Criteria;
import joshie.progression.criteria.Reward;
import joshie.progression.criteria.Tab;
import joshie.progression.handlers.APIHandler;
import joshie.progression.player.PlayerTeam.TeamType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldSavedData;

public class PlayerSavedData extends WorldSavedData {
    private HashMap<UUID, PlayerTeam> teams = Maps.newHashMap();
    private HashMap<PlayerTeam, PlayerDataServer> data = Maps.newHashMap();

    public PlayerSavedData(String data) {
        super(data);
    }

    public Collection<PlayerDataServer> getPlayerData() {
        return data.values();
    }

    public PlayerDataServer getServerPlayer(UUID uuid) {
        PlayerTeam team = teams.get(uuid);
        if (team == null) {
            team = new PlayerTeam(TeamType.SINGLE, uuid);
            teams.put(uuid, team);
        }

        PlayerDataServer data = this.data.get(team);
        if (data == null) {
            data = new PlayerDataServer(team);
            this.data.put(team, data);
            this.markDirty();
            //If this team never existed before
            //Loop through all the rewards loaded and init them with player data
            for (Tab tab: APIHandler.getTabs().values()) {
                for (Criteria c: tab.getCriteria()) {
                    for (Reward r: c.rewards) {
                        if (r.getType() != null) {
                            data.getAbilities().setCustomData(r.getType().getNBTKey(), r.getType().getDefaultTags(new NBTTagCompound()));
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
