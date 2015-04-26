package joshie.progression.commands;

import joshie.progression.network.PacketHandler;
import joshie.progression.network.PacketReload;
import net.minecraft.command.ICommandSender;

public class CommandReload extends AbstractCommand {
    @Override
    public String getCommandName() {
        return "reload";
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public boolean processCommand(ICommandSender sender, String[] parameters) {
        PacketHandler.sendToServer(new PacketReload());
        return true;
    }
}
