package joshie.progression.network;

import io.netty.buffer.ByteBuf;
import joshie.progression.helpers.SplitHelper;
import joshie.progression.json.DefaultSettings;
import joshie.progression.json.JSONLoader;
import joshie.progression.json.Options;
import joshie.progression.network.core.PacketPart;
import joshie.progression.network.core.PacketSyncStringArray;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;

import static joshie.progression.network.core.PacketPart.REQUEST_DATA;
import static joshie.progression.network.core.PacketPart.SEND_DATA;

@Packet
public class PacketSyncJSONToServer extends PacketSyncStringArray {
    private static HashMap<Long, String[]> serverList = new HashMap();
    private long timestamp;

    public PacketSyncJSONToServer() {}

    public PacketSyncJSONToServer(PacketPart part, long timestamp) {
        super(part);
        this.timestamp = timestamp;
    }

    public PacketSyncJSONToServer(PacketPart part, String text, int index, long timestamp) {
        super(part, text, index);
        this.timestamp = timestamp;
    }

    @Override
    public void toBytes(ByteBuf to) {
        to.writeLong(timestamp);
        super.toBytes(to);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        timestamp = from.readLong();
        super.fromBytes(from);
    }

    @Override
    public void receivedHashcode(EntityPlayer player) {}

    @Override
    public void receivedLengthRequest(EntityPlayer player) {}

    @Override
    public void receivedStringLength(EntityPlayer player) {
        if (Options.editor) {
            String[] server = new String[integer]; //Build up the string value from the name
            serverList.put(timestamp, server); //Create and place in hashmap
            PacketHandler.sendToClient(new PacketSyncJSONToServer(REQUEST_DATA, timestamp), player);
        }
    }

    @Override
    public void receivedDataRequest(EntityPlayer player) {
        //Grab the data and send it
        if (Options.editor) {
            String json = JSONLoader.getClientTabJsonData();
            String[] client = SplitHelper.splitStringEvery(json, 5000);
            for (int i = 0; i < client.length; i++) {
                PacketHandler.sendToServer(new PacketSyncJSONToServer(SEND_DATA, client[i], i, timestamp));
            }
        }
    }

    @Override
    public void receivedData(EntityPlayer player) {
        if (serverList.get(timestamp) != null && serverList.get(timestamp).length > integer) {
            String[] server = serverList.get(timestamp);
            server[integer] = text;
            //Now check if any parts are null
            boolean all = true;
            for (String s : server) {
                if (s == null) all = false;
            }

            //Received all the data
            if (all) {
                StringBuilder builder = new StringBuilder();
                for (String s : server) {
                    builder.append(s);
                }

                String json = builder.toString();
                DefaultSettings settings = JSONLoader.getGson().fromJson(json, DefaultSettings.class);
                JSONLoader.serverHashcode = (int) timestamp; //For resyncing purposes < and v
                JSONLoader.serverTabJsonData = SplitHelper.splitStringEvery(json, JSONLoader.MAX_LENGTH);
                //Now that we have saved the newer data, we should reload it all
                PacketReload.handle(settings, false); //Reload everything
                JSONLoader.saveData(false); //Save to the server
                serverList.remove(timestamp); //Reset
            }
        }
    }
}