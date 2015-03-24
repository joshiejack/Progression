package joshie.crafting.network;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.helpers.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

public class PacketReload extends PacketAction implements IMessageHandler<PacketReload, IMessage> {
	@Override
	public IMessage onMessage(PacketReload message, MessageContext ctx) {
		//Load JSON Server Side, Then Send packet to client to load it all again
		//We now have up to date data on what is in the mappings
		CraftingAPI.registry.reloadJson();
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			PacketHandler.sendToEveryone(new PacketReload());
			for (EntityPlayer player: PlayerHelper.getAllPlayers()) {
				CraftingAPI.players.getPlayerData(PlayerHelper.getUUIDForPlayer(player)).getMappings().remap();
			}
		}
		
		return null;
	}
}
