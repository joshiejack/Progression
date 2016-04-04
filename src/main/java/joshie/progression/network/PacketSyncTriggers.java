package joshie.progression.network;

import io.netty.buffer.ByteBuf;
import joshie.progression.api.criteria.ITriggerProvider;
import joshie.progression.handlers.APIHandler;
import joshie.progression.network.core.PenguinPacket;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PacketSyncTriggers extends PenguinPacket {
    private Set<ITriggerProvider> triggers;
    private boolean overwrite;

    public PacketSyncTriggers() {}
    public PacketSyncTriggers(Set<ITriggerProvider> triggers) {
        this.overwrite = true;
        this.triggers = triggers;
    }

    public PacketSyncTriggers(ITriggerProvider trigger) {
        this.overwrite = false;
        this.triggers = new HashSet();
        this.triggers.add(trigger);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(overwrite);
        buf.writeInt(triggers.size());
        for (ITriggerProvider trigger: triggers) {
            ByteBufUtils.writeUTF8String(buf, trigger.getUniqueID().toString());
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        overwrite = buf.readBoolean();
        int size = buf.readInt();
        triggers = new HashSet();
        for (int i = 0; i < size; i++) {
            ITriggerProvider trigger = APIHandler.getCache().getTriggerFromUUID(UUID.fromString(ByteBufUtils.readUTF8String(buf)));
            if (trigger != null) {
                triggers.add(trigger);
            }
        }
    }

    @Override
	public void handlePacket(EntityPlayer player) {   
        PlayerTracker.getClientPlayer().getMappings().markTriggerAsCompleted(overwrite, triggers);
    }
}
