package joshie.progression.player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class PlayerTeam {
    public static enum TeamType {
        SINGLE, TEAM;
    }

    private Set<UUID> members = new HashSet();
    private TeamType type;
    private UUID owner;
    private boolean isActive = true;

    public PlayerTeam() {}
    public PlayerTeam(TeamType type, UUID owner) {
        this.owner = owner;
    }

    public UUID getOwner() {
        return owner;
    }

    public Set<UUID> getMembers() {
        return members;
    }

    public void addMember(UUID uuid) {
        members.add(uuid);
    }
    
    /** Whether or not this data is used by anyone **/
    public boolean isActive() {
        return isActive;
    }
    
    public void readFromNBT(NBTTagCompound tag) {
        type = tag.getBoolean("IsSingleTeam") ? TeamType.SINGLE: TeamType.TEAM;
        if (tag.hasKey("Owner")) owner = UUID.fromString(tag.getString("Owner"));
        else if (tag.hasKey("UUID-Most")) owner = new UUID(tag.getLong("UUID-Most"), tag.getLong("UUID-Least"));
        isActive = tag.getBoolean("IsActive");
        
        NBTTagList list = tag.getTagList("Team", 10);
        for (int j = 0; j < list.tagCount(); j++) {
            addMember(UUID.fromString(list.getStringTagAt(j)));
        }
    }

    public void writeToNBT(NBTTagCompound tag) {
        tag.setBoolean("IsSingleTeam", type == TeamType.SINGLE);
        tag.setString("Owner", owner.toString());
        tag.setBoolean("IsActive", isActive);
        
        NBTTagList list = new NBTTagList();
        for (UUID uuid : members) {
            list.appendTag(new NBTTagString(uuid.toString()));
        }

        tag.setTag("Team", list);
    }
}
