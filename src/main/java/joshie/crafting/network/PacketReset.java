package joshie.crafting.network;

import joshie.crafting.CraftingMod;
import joshie.crafting.CraftingRemapper;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.PlayerHelper;
import joshie.crafting.json.Options;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

public class PacketReset extends PacketAction implements IMessageHandler<PacketReset, IMessage> {
    @Override
    public IMessage onMessage(PacketReset message, MessageContext ctx) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            ClientHelper.getPlayer().addChatComponentMessage(new ChatComponentText("All player data for Progression on was reset."));
        } else {
            if (Options.editor) {
                CraftingMod.instance.createWorldData(); //Recreate the world data, Wiping out any saved information for players
                CraftingRemapper.reloadServerData();
                for (EntityPlayer player: PlayerHelper.getAllPlayers()) {
                    CraftingRemapper.onPlayerConnect((EntityPlayerMP) player);
                }
                
                PacketHandler.sendToEveryone(new PacketReset());
            }
        }

        return null;
    }
}
