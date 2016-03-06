package joshie.progression.network;

import io.netty.buffer.ByteBuf;
import joshie.progression.network.core.PenguinPacket;
import joshie.progression.player.DataStats;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;

public class PacketSyncAbilities extends PenguinPacket {
	private DataStats abilities;
    
    public PacketSyncAbilities() {}
    public PacketSyncAbilities(DataStats abilities) {
        this.abilities = abilities;
    }

    @Override
    public void toBytes(ByteBuf buf) {
    	abilities.toBytes(buf);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    	abilities = new DataStats();
    	abilities.fromBytes(buf);
    }
    
    @Override
	public void handlePacket(EntityPlayer player) {     
    	PlayerTracker.getClientPlayer().setAbilities(abilities);
    }
}
