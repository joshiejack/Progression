package joshie.progression.commands;

import joshie.progression.Progression;
import joshie.progression.criteria.triggers.TriggerChangeGui;
import joshie.progression.helpers.ChatHelper;
import joshie.progression.network.PacketDisplayChat;
import joshie.progression.network.PacketHandler;
import net.minecraft.command.ICommandSender;

import static net.minecraft.util.EnumChatFormatting.BLUE;

@Command
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
            ChatHelper.displayChat(BLUE + Progression.translate("display.gui") + " " + TriggerChangeGui.toggleDebug());
        } else PacketHandler.sendToClient(new PacketDisplayChat(BLUE + Progression.translate("display.gui") + " " + TriggerChangeGui.toggleDebug()), sender);

        return true;
    }
}
