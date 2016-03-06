package joshie.progression.commands;

import joshie.progression.lib.CommandLevel;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public abstract class AbstractCommand implements Comparable {
    public CommandLevel getPermissionLevel() {
        return CommandLevel.OP_AFFECT_GAMEPLAY;
    }
    
    @Override
    public int compareTo(Object o) {
        return getCommandName().compareTo(((AbstractCommand)o).getCommandName());
    }

    public abstract String getCommandName();

    public abstract boolean processCommand(ICommandSender sender, String[] parameters) throws CommandException;

    public String getUsage() {
        return "";
    }
}