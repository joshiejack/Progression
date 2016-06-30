package joshie.progression.player;

import joshie.progression.api.IPlayerTeam;
import joshie.progression.gui.editors.ITextEditable;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.network.PacketHandler;
import joshie.progression.network.PacketSyncTeam;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

public class PlayerTeam implements ITextEditable, IPlayerTeam {
    public enum TeamType {
        SINGLE, TEAM;
    }

    private Set<UUID> invited = new HashSet();
    private Set<UUID> members = new HashSet();
    private TeamType type;
    private UUID owner;
    private boolean isActive = true;
    private boolean multipleRewards = true;
    private boolean isTrueTeam = true;
    private String name;

    private transient HashMap<UUID, EntityPlayer> teamCache = new HashMap();

    public PlayerTeam() {}

    public PlayerTeam(TeamType type, String name, UUID owner) {
        this.owner = owner;
        this.type = type;
        if (name != null) {
            this.name = name;
        } else this.name = "Single Player";
    }

    public boolean isInvited(UUID uuid) {
        return invited.contains(uuid);
    }

    public void addToInvited(UUID uuid) {
        invited.add(uuid);
    }

    public TeamType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public UUID getOwner() {
        return owner;
    }

    //For quicker access
    public void rebuildTeamCache() {
        teamCache = new HashMap();
        EntityPlayer owner = PlayerHelper.getPlayerFromUUID(false, getOwner());
        if (owner != null) teamCache.put(getOwner(), owner);
        for (UUID uuid: getMembers()) {
            EntityPlayer member = (EntityPlayer) PlayerHelper.getPlayerFromUUID(false, uuid);
            if (member != null) teamCache.put(uuid, member);
        }
    }

    private boolean hasIllegalUUIDInCache() {
        for (UUID uuid: teamCache.keySet()) {
            if (getOwner() == uuid || getMembers().contains(uuid)) continue;
            return true;
        }

        return false;
    }

    @Override
    public Collection<EntityPlayer> getTeamEntities() {
        if (teamCache.isEmpty() || hasIllegalUUIDInCache()) {
            rebuildTeamCache();
        }

        return teamCache.values();
    }

    @Override
    public boolean isTrueTeam() {
        return isTrueTeam;
    }

    @Override
    public boolean isSingle() {
        return type == TeamType.SINGLE;
    }
    
    public boolean giveMultipleRewards() {
        return multipleRewards;
    }

    public void toggleMultiple() {
        multipleRewards = !multipleRewards;
        syncChanges(Side.CLIENT);
    }
    
    public void toggleIsTrueTeam() {
        isTrueTeam = !isTrueTeam;
        syncChanges(Side.CLIENT);
    }

    public boolean isOwner(EntityPlayer player) {
        return PlayerHelper.getUUIDForPlayer(player).equals(getOwner());
    }

    public Set<UUID> getMembers() {
        return members;
    }

    public Set<UUID> getEveryone() {
        Set<UUID> everyone = new LinkedHashSet();
        everyone.add(getOwner());
        if (giveMultipleRewards()) {
            everyone.addAll(getMembers());
        }

        return everyone;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getTextField() {
        return this.name;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void setTextField(String arg0) {
        boolean sync = false;
        if (!this.name.equals(arg0)) sync = true;
        this.name = arg0;

        if (sync) syncChanges(Side.CLIENT);
    }

    public void addMember(UUID uuid) {
        members.add(uuid);
    }

    //Returns false if this team no longer exists
    public boolean removeMember(UUID uuid) {
        if (uuid == getOwner() && members.size() == 0) {
            return false;
        } else if (uuid == getOwner()) { //If the owner leaves, then the next level member becomes the owner
            for (UUID member: members) {
                owner = member;
                break;
            }

            //Remove the new leader from members
            members.remove(owner);
        } else members.remove(uuid);

        //Otherwise, Remove
        return true;
    }

    /** Whether or not this data is used by anyone **/
    public boolean isActive() {
        return isActive;
    }

    /** Should only be called client side, called to update the data on the server **/
    public void syncChanges(Side side) {
        if (side == Side.CLIENT) PacketHandler.sendToServer(new PacketSyncTeam(this));
        else if (side == Side.SERVER) {
            for (EntityPlayerMP player : PlayerHelper.getPlayersFromUUID(getOwner())) {
                PacketHandler.sendToClient(new PacketSyncTeam(this), player);
            }
        }
    }

    public void readFromNBT(NBTTagCompound tag) {
        name = tag.getString("Name");
        multipleRewards = tag.getBoolean("MultipleRewards");
        isTrueTeam = tag.getBoolean("CountWholeTeam");
        type = tag.getBoolean("IsSingleTeam") ? TeamType.SINGLE : TeamType.TEAM;
        if (tag.hasKey("Owner")) owner = UUID.fromString(tag.getString("Owner"));
        else if (tag.hasKey("UUID-Most")) owner = new UUID(tag.getLong("UUID-Most"), tag.getLong("UUID-Least"));
        isActive = tag.getBoolean("IsActive");

        members = new HashSet();
        NBTTagList list = tag.getTagList("TeamMembers", 8);
        for (int j = 0; j < list.tagCount(); j++) {
            addMember(UUID.fromString(list.getStringTagAt(j)));
        }
    }

    public void writeToNBT(NBTTagCompound tag) {
        tag.setString("Name", name);
        tag.setBoolean("MultipleRewards", multipleRewards);
        tag.setBoolean("CountWholeTeam", isTrueTeam);
        tag.setBoolean("IsSingleTeam", type == TeamType.SINGLE);
        tag.setString("Owner", owner.toString());
        tag.setBoolean("IsActive", isActive);

        NBTTagList list = new NBTTagList();
        for (UUID uuid : members) {
            list.appendTag(new NBTTagString(uuid.toString()));
        }

        tag.setTag("TeamMembers", list);
    }
}
