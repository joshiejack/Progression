package joshie.progression.commands;

import joshie.progression.helpers.MCClientHelper;
import joshie.progression.network.PacketHandler;
import joshie.progression.network.PacketReset;
import net.minecraft.command.ICommandSender;

public class CommandReset extends AbstractCommand {
    @Override
    public String getCommandName() {
        return "reset";
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public boolean processCommand(ICommandSender sender, String[] parameters) {
        if (sender.getEntityWorld().isRemote) {
            if (parameters.length >= 1) PacketHandler.sendToServer(new PacketReset(parameters[0]));
            else PacketHandler.sendToServer(new PacketReset());
        } else {
            if (parameters.length >= 1) PacketReset.handle(MCClientHelper.getPlayer(), true, parameters[0]);
            else PacketReset.handle(MCClientHelper.getPlayer(), false, null);
        }

        return true;
    }
}
