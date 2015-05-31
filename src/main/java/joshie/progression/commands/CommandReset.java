package joshie.progression.commands;

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
            PacketHandler.sendToServer(new PacketReset());
        } else PacketReset.handle();

        return true;
    }
}
