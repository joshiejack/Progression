package joshie.progression.network;

import io.netty.buffer.ByteBuf;
import joshie.progression.network.core.PenguinPacket;
import joshie.progression.player.PlayerSavedData.TeamAction;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.UUID;

public class PacketChangeTeam extends PenguinPacket {
    private TeamAction action;
    private UUID team;

    public PacketChangeTeam() {}
    public PacketChangeTeam(TeamAction action) {
        this.action = action;
    }

    public PacketChangeTeam(TeamAction action, UUID team) {
        this(action);
        this.team = team;
    }

	@Override
	public void toBytes(ByteBuf to) {
        to.writeInt(action.ordinal());
        if (team != null) {
            to.writeBoolean(true);
            ByteBufUtils.writeUTF8String(to, team.toString());
        } else to.writeBoolean(false);
    }

	@Override
	public void fromBytes(ByteBuf from) {
        action = TeamAction.values()[from.readInt()];
        if (from.readBoolean()) {
            team = UUID.fromString(ByteBufUtils.readUTF8String(from));
        }
    }

	@Override
	public void handlePacket(EntityPlayer player) {
        PlayerTracker.joinTeam(player, action, team);
	}
}
