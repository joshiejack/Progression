package joshie.crafting.network;

import io.netty.buffer.ByteBuf;
import joshie.crafting.data.DataClient;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncKillings implements IMessage, IMessageHandler<PacketSyncKillings, IMessage> {
	private String entity;
	int killings;

	public PacketSyncKillings() {}
	public PacketSyncKillings(String entity, int killings) {
		this.entity = entity;
		this.killings = killings;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, entity);
		buf.writeInt(killings);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		entity = ByteBufUtils.readUTF8String(buf);
		killings = buf.readInt();
	}

	@Override
	public IMessage onMessage(PacketSyncKillings message, MessageContext ctx) {
		DataClient.INSTANCE.setKillings(message.entity, message.killings);
		return null;
	}
}
