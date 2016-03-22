package joshie.progression.network;

import io.netty.buffer.ByteBuf;
import joshie.progression.api.criteria.IProgressionCriteria;
import joshie.progression.handlers.APIHandler;
import joshie.progression.network.core.PenguinPacket;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketSyncImpossible extends PenguinPacket {
    private IProgressionCriteria[] criteria;

    public PacketSyncImpossible() {}

    public PacketSyncImpossible(IProgressionCriteria[] criteria) {
        this.criteria = criteria;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(criteria.length);
        for (IProgressionCriteria tech : criteria) {
            ByteBufUtils.writeUTF8String(buf, tech.getUniqueName());
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int size = buf.readInt();
        criteria = new IProgressionCriteria[size];
        for (int i = 0; i < size; i++) {
            criteria[i] = APIHandler.getCriteriaFromName(ByteBufUtils.readUTF8String(buf));
        }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        PlayerTracker.getClientPlayer().getMappings().setImpossibles(criteria);
    }
}
