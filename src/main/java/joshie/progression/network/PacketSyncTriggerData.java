package joshie.progression.network;

import io.netty.buffer.ByteBuf;
import joshie.progression.api.special.IStoreTriggerData;
import joshie.progression.network.core.PenguinPacket;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.HashMap;
import java.util.UUID;

public class PacketSyncTriggerData extends PenguinPacket {
    public static class DataPair {
        public UUID uuid;
        public NBTTagCompound data;

        public DataPair(){}
        public DataPair(UUID uuid, NBTTagCompound data) {
            this.uuid = uuid;
            this.data = data;
        }

        public void toBytes(ByteBuf buf) {
            ByteBufUtils.writeUTF8String(buf, uuid.toString());
            ByteBufUtils.writeTag(buf, data);
        }

        public void fromBytes(ByteBuf buf) {
            uuid = UUID.fromString(ByteBufUtils.readUTF8String(buf));
            data = ByteBufUtils.readTag(buf);
        }
    }

    private boolean overwrite;
    private DataPair[] data;

    public PacketSyncTriggerData() {}
    public PacketSyncTriggerData(HashMap<UUID, IStoreTriggerData> triggerData) {
        this.overwrite = true;
        this.data = new DataPair[triggerData.size()];
        int position = 0;
        for (UUID uuid: triggerData.keySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            triggerData.get(uuid).writeDataToNBT(tag);
            this.data[position] = new DataPair(uuid, tag);
            position++;
        }
    }

    public PacketSyncTriggerData(UUID uuid, NBTTagCompound data) {
        this.overwrite = false;
        this.data = new DataPair[1];
        this.data[0] = new DataPair(uuid, data);
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
