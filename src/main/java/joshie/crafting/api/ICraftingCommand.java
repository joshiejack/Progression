package joshie.crafting.api;

import net.minecraft.command.ICommandSender;

public interface ICraftingCommand extends Comparable {
    public String getCommandName();

    public CommandLevel getPermissionLevel();

    /** Returns true if the command was successfully processed **/
    public boolean processCommand(ICommandSender sender, String[] parameters);

    public String getUsage();
}