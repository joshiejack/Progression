package joshie.progression.network;

import io.netty.buffer.ByteBuf;
import joshie.progression.handlers.APIHandler;
import joshie.progression.handlers.RemappingHandler;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.json.JSONLoader;
import joshie.progression.network.core.PenguinPacket;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.UUID;

import static joshie.progression.network.PacketSyncJSONToClient.Section.*;

public class PacketSyncJSONToClient extends PenguinPacket {
    public static enum Section {
        RESYNC, SEND_HASH, FAILED_HASH, SEND_LENGTH, RECEIVED_LENGTH, SEND_STRING, COMPLETE;
    }

    public PacketSyncJSONToClient() {}

    public PacketSyncJSONToClient(Section section) {
        this.section = section;
    }

    public PacketSyncJSONToClient(Section section, int length) {
        this.section = section;
        this.integer = length;
    }

    private Section section;
    private int integer = -1;
    private String string = null;

    public PacketSyncJSONToClient(Section section, int position, String data) {
        this.section = section;
        this.integer = position;
        this.string = data;
    }



    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(section.ordinal());
        buf.writeInt(integer);
        if (string != null) {
            buf.writeBoolean(true);
            writeGzipString(buf, string);
        } else buf.writeBoolean(false);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        section = Section.values()[buf.readInt()];
        integer = buf.readInt();
        if (buf.readBoolean()) {
            string = readGzipString(buf);
        }
    }

    @Override
    public void handlePacket(EntityPlayer thePlayer) {
        if (section == RESYNC) {
            //Called from a command, sends all the data
            for (EntityPlayer player: PlayerHelper.getAllPlayers()) {
                RemappingHandler.onPlayerConnect((EntityPlayerMP) player);
            }
        } else if (section == SEND_HASH) { //Called when a player logins, sends the client the hash
            JSONLoader.serverName = string; 
            String json = JSONLoader.getClientTabJsonData();            
            if (json.hashCode() == integer) {
                //If we set the json correctly
                if (JSONLoader.setTabsAndCriteriaFromString(json, false)) {
                    PacketHandler.sendToServer(new PacketSyncJSONToClient(COMPLETE));
                }
            } else PacketHandler.sendToServer(new PacketSyncJSONToClient(FAILED_HASH));
        } else if (section == FAILED_HASH) {
            PacketHandler.sendToClient(new PacketSyncJSONToClient(Section.SEND_LENGTH, JSONLoader.serverTabJsonData.length), thePlayer);
        } else if (section == SEND_LENGTH) { //Clientside set the data for receival of this packet
            JSONLoader.clientTabJsonData = new String[integer];
            APIHandler.resetAPIHandler(true); //Reset the cache once we've received the length
            PacketHandler.sendToServer(new PacketSyncJSONToClient(RECEIVED_LENGTH));
        } else if (section == RECEIVED_LENGTH) {
            for (int i = 0; i < JSONLoader.serverTabJsonData.length; i++) {
                PacketHandler.sendToClient(new PacketSyncJSONToClient(SEND_STRING, i, JSONLoader.serverTabJsonData[i]), thePlayer);
            } //Now that we have received the data, send more
        } else if (section == SEND_STRING) { //Client has now been sent the string
            JSONLoader.clientTabJsonData[integer] = string;
            for (String s : JSONLoader.clientTabJsonData) {
                if (s == null) return;
            }

            //All data has arrived, on the client
            StringBuffer result = new StringBuffer();
            for (int i = 0; i < JSONLoader.clientTabJsonData.length; i++) {
                result.append(JSONLoader.clientTabJsonData[i]);
            }

            //If we set the json correctly
            if (JSONLoader.setTabsAndCriteriaFromString(result.toString(), true)) {
                PacketHandler.sendToServer(new PacketSyncJSONToClient(COMPLETE));
            }
        } else if (section == Section.COMPLETE) {
            UUID uuid = PlayerHelper.getUUIDForPlayer(thePlayer);
            //Sends all the data to do with this player to the client, so it's up to date
            PlayerTracker.getPlayerData(uuid, false).getMappings().syncToClient((EntityPlayerMP) thePlayer);
        }
    }
}
