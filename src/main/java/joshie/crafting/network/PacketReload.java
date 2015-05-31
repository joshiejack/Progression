package joshie.crafting.network;

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

public class PacketReload extends PacketAction implements IMessageHandler<PacketReload, IMessage> {
    @Override
    public IMessage onMessage(PacketReload message, MessageContext ctx) {
        PacketReload.handle();
        return null;
    }
    
    public static void handle() {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            ClientHelper.getPlayer().addChatComponentMessage(new ChatComponentText("Progression data was reloaded."));
        } else {
            if (Options.editor) {
                //Perform a reset of all the data serverside
                CraftingRemapper.reloadServerData();
                for (EntityPlayer player: PlayerHelper.getAllPlayers()) {
                    CraftingRemapper.onPlayerConnect((EntityPlayerMP) player);
                }
            }
        }
    }
}
