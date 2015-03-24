package joshie.crafting.commands;

import joshie.crafting.network.PacketHandler;
import joshie.crafting.network.PacketReset;
import net.minecraft.command.ICommandSender;

public class CommandReset extends CommandBase {
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
        PacketHandler.sendToServer(new PacketReset());
        return true;
    }
}
