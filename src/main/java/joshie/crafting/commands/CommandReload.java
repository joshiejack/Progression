package joshie.crafting.commands;

import joshie.crafting.network.PacketHandler;
import joshie.crafting.network.PacketReload;
import net.minecraft.command.ICommandSender;

public class CommandReload extends CommandBase {
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
