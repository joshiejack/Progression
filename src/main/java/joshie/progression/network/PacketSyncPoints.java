package joshie.progression.network;

import joshie.progression.network.core.PacketNBT;
import joshie.progression.player.PlayerTracker;
import joshie.progression.player.data.Points;
import net.minecraft.entity.player.EntityPlayer;

public class PacketSyncPoints extends PacketNBT {
    public PacketSyncPoints() {}
    public PacketSyncPoints(INBTWritable readable) {
        super(readable);
    }
    
    @Override
    public void handlePacket(EntityPlayer player) {
        PlayerTracker.getClientPlayer().setPoints(new Points().readFromNBT(tag));
    }
}
