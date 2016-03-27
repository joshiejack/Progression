package joshie.progression.network;

import io.netty.buffer.ByteBuf;
import joshie.progression.api.criteria.IProgressionTrigger;
import joshie.progression.handlers.APIHandler;
import joshie.progression.network.core.PenguinPacket;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.HashMap;
import java.util.UUID;

public class PacketSyncTriggerData extends PenguinPacket {
    public static class DataPair {
        public IProgressionTrigger trigger;
        public NBTTagCompound data;

        public DataPair(){}
        public DataPair(IProgressionTrigger trigger, NBTTagCompound data) {
            this.trigger = trigger;
            this.data = data;
        }

        public void toBytes(ByteBuf buf) {
            ByteBufUtils.writeUTF8String(buf, trigger.getUniqueID().toString());
            ByteBufUtils.writeTag(buf, data);
        }

        public void fromBytes(ByteBuf buf) {
            trigger = APIHandler.getTriggerFromUUID(UUID.fromString(ByteBufUtils.readUTF8String(buf)));
            data = ByteBufUtils.readTag(buf);
        }
    }

    private boolean overwrite;
    private DataPair[] data;

    public PacketSyncTriggerData() {}
    public PacketSyncTriggerData(HashMap<IProgressionTrigger, NBTTagCompound> triggers) {
        this.overwrite = true;
        this.data = new DataPair[triggers.size()];
        int position = 0;
        for (IProgressionTrigger trigger: triggers.keySet()) {
            this.data[position] = new DataPair(trigger, triggers.get(trigger));
            position++;
        }
    }

    public PacketSyncTriggerData(IProgressionTrigger trigger, NBTTagCompound data) {
        this.overwrite = false;
        this.data = new DataPair[1];
        this.data[0] = new DataPair(trigger, data);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(overwrite);
        buf.writeInt(data.length);
        for (DataPair pair: data) {
            pair.toBytes(buf);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        overwrite = buf.readBoolean();
        int size = buf.readInt();
        data = new DataPair[size];
        for (int i = 0; i < size; i++) {
            data[i] = new DataPair();
            data[i].fromBytes(buf);
        }
    }


    @Override
    public void handlePacket(EntityPlayer player) {
        PlayerTracker.getClientPlayer().getMappings().setTriggerData(overwrite, data);
    }
}
