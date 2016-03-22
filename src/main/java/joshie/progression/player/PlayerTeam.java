package joshie.progression.player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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

public class PlayerTeam implements ITextEditable {
    public static enum TeamType {
        SINGLE, TEAM;
    }

    private Set<UUID> members = new HashSet();
    private TeamType type;
    private UUID owner;
    private boolean isActive = true;
    private boolean multipleRewards = false;
    private boolean isPublic = false;
    private String name;

    public PlayerTeam() {}

    public PlayerTeam(TeamType type, UUID owner) {
        this.owner = owner;
        this.type = type;
        EntityPlayer player = PlayerHelper.getPlayerFromUUID(owner);
        if (player != null) {
            this.name = player.getGameProfile().getName();
        } else this.name = "Single Player";
    }

    public TeamType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public UUID getOwner() {
        return owner;
    }
    
    public boolean giveMultipleRewards() {
        return multipleRewards;
    }
    
    public boolean isPublic() {
        return isPublic;
    }
    
    public void toggleMultiple() {
        multipleRewards = !multipleRewards;
        syncChanges(Side.CLIENT);
    }
    
    public void toggleIsPublic() {
        isPublic = !isPublic;
        syncChanges(Side.CLIENT);
    }

    public boolean isOwner(EntityPlayer player) {
        return PlayerHelper.getUUIDForPlayer(player).equals(getOwner());
    }

    public Set<UUID> getMembers() {
        return members;
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

    @SideOnly(Side.CLIENT)
    public void removeMember(UUID uuid) {
        if (members.remove(uuid)) {
            syncChanges(Side.CLIENT);
        }
    }

    @SideOnly(Side.CLIENT)
    public void addMember(UUID uuid) {
        if (members.add(uuid)) {
            syncChanges(Side.CLIENT);
        }
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
        isPublic = tag.getBoolean("IsPublic");
        multipleRewards = tag.getBoolean("MultipleRewards");
        type = tag.getBoolean("IsSingleTeam") ? TeamType.SINGLE : TeamType.TEAM;
        if (tag.hasKey("Owner")) owner = UUID.fromString(tag.getString("Owner"));
        else if (tag.hasKey("UUID-Most")) owner = new UUID(tag.getLong("UUID-Most"), tag.getLong("UUID-Least"));
        isActive = tag.getBoolean("IsActive");

        members = new HashSet();
        NBTTagList list = tag.getTagList("Team", 8);
        for (int j = 0; j < list.tagCount(); j++) {
            addMember(UUID.fromString(list.getStringTagAt(j)));
        }
    }

    public void writeToNBT(NBTTagCompound tag) {
        tag.setString("Name", name);
        tag.setBoolean("IsPublic", isPublic);
        tag.setBoolean("MultipleRewards", multipleRewards);
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
