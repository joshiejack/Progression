package joshie.progression.commands;

import joshie.progression.criteria.triggers.TriggerChangeGui;
import joshie.progression.helpers.ChatHelper;
import joshie.progression.network.PacketDebugGUI;
import joshie.progression.network.PacketHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;

public class CommandGui extends AbstractCommand {
    @Override
    public String getCommandName() {
        return "gui";
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public boolean processCommand(ICommandSender sender, String[] parameters) {
        if (sender.getEntityWorld().isRemote) {
            ChatHelper.displayChat(EnumChatFormatting.BLUE + "Displaying class names for guis was set to " + TriggerChangeGui.toggleDebug());
        } else PacketHandler.sendToClient(new PacketDebugGUI(), sender);

        return true;
    }
}
