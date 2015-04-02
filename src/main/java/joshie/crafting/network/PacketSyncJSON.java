package joshie.crafting.network;

import static joshie.crafting.network.PacketSyncJSON.Section.COMPLETE;
import static joshie.crafting.network.PacketSyncJSON.Section.RECEIVED_LENGTH;
import static joshie.crafting.network.PacketSyncJSON.Section.SEND_LENGTH;
import static joshie.crafting.network.PacketSyncJSON.Section.SEND_STRING;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.helpers.PlayerHelper;
import joshie.crafting.json.JSONLoader;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncJSON implements IMessage, IMessageHandler<PacketSyncJSON, IMessage> {
    public static enum Section {
        SEND_LENGTH, RECEIVED_LENGTH, SEND_STRING, COMPLETE;
    }

    private Section section;

    public PacketSyncJSON() {}

    public PacketSyncJSON(Section section) {
        this.section = section;
    }

    private int length;

    public PacketSyncJSON(int length) {
        this.section = SEND_LENGTH;
        this.length = length;
    }

    private int position;
    private String data;

    public PacketSyncJSON(int position, String data) {
        this.section = SEND_STRING;
        this.position = position;
        this.data = data;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(section.ordinal());
        if (section == SEND_LENGTH) {
            buf.writeInt(length);
        } else if (section == SEND_STRING) {
            buf.writeInt(position);
            ByteBufUtils.writeUTF8String(buf, data);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        section = Section.values()[buf.readInt()];
        if (section == SEND_LENGTH) {
            length = buf.readInt();
        } else if (section == SEND_STRING) {
            position = buf.readInt();
            data = ByteBufUtils.readUTF8String(buf);
        }
    }

    @Override
    public IMessage onMessage(PacketSyncJSON message, MessageContext ctx) {        
        if (message.section == SEND_LENGTH) { //Clientside set the data for receival of this packet
            JSONLoader.clientTabJsonData = new String[message.length];
            PacketHandler.sendToServer(new PacketSyncJSON(RECEIVED_LENGTH));
        } else if (message.section == RECEIVED_LENGTH) {
            for (int i = 0; i < JSONLoader.serverTabJsonData.length; i++) {
                PacketHandler.sendToClient(new PacketSyncJSON(i, JSONLoader.serverTabJsonData[i]), ctx.getServerHandler().playerEntity);
            } //Now that we have received the data, send more
        } else if (message.section == SEND_STRING) { //Client has now been sent the string
            JSONLoader.clientTabJsonData [message.position] = message.data;
            for (String s : JSONLoader.clientTabJsonData) {
                if (s == null) return null;
            }

            //All data has arrived, on the client
            StringBuffer result = new StringBuffer();
            for (int i = 0; i < JSONLoader.clientTabJsonData.length; i++) {
                result.append(JSONLoader.clientTabJsonData[i]);
            }

            //If we set the json correctly
            if (JSONLoader.setTabsAndCriteriaFromString(result.toString())) {
                PacketHandler.sendToServer(new PacketSyncJSON(COMPLETE));
            }
        } else if (message.section == Section.COMPLETE) {
            UUID uuid = PlayerHelper.getUUIDForPlayer(ctx.getServerHandler().playerEntity);
            //Sends all the data to do with this player to the client, so it's up to date
            CraftingAPI.players.getPlayerData(uuid).getMappings().syncToClient(ctx.getServerHandler().playerEntity);
        }

        return null;
    }
}
