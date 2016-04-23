package joshie.progression.network;

import io.netty.buffer.ByteBuf;
import joshie.progression.helpers.ChatHelper;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.network.core.PenguinPacket;
import joshie.progression.player.PlayerTeam;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.UUID;

import static joshie.progression.gui.core.GuiList.GROUP_EDITOR;

public class PacketInvitePlayer extends PenguinPacket {
    private UUID teamOwner;
    private String teamName;
    private String username;

    public PacketInvitePlayer() {}
    public PacketInvitePlayer(UUID teamOwner, String team, String username) {
        this.teamOwner = teamOwner;
        this.teamName = team;
        this.username = username;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        writeGzipString(buf, teamOwner.toString());
        writeGzipString(buf, teamName);
        writeGzipString(buf, username);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        teamOwner = UUID.fromString(readGzipString(buf));
        teamName = readGzipString(buf);
        username = readGzipString(buf);
    }

    @Override
    public void handlePacket(EntityPlayer sender) {
        if (!sender.worldObj.isRemote) {
            PlayerTeam team = PlayerTracker.getPlayerData(sender).getTeam();
            for (EntityPlayerMP player: PlayerHelper.getAllPlayers()) {
                if (player.getName().equals(username)) {
                    PacketHandler.sendToClient(new PacketInvitePlayer(teamOwner, teamName, username), player);
                    team.addToInvited(PlayerHelper.getUUIDForPlayer(player));
                    break;
                }
            }
        } else {
            GROUP_EDITOR.addInvite(teamOwner, teamName);
            ChatHelper.displayChat("You were invited to join the team: teamName, if you wish to join this team, use the book or type /progression team join teamName".replace("teamName", teamName));
        }
    }
}
