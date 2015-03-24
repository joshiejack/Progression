package joshie.crafting.commands;

import joshie.crafting.api.CommandLevel;
import joshie.crafting.api.ICraftingCommand;

public abstract class CommandBase implements ICraftingCommand {
    @Override
    public CommandLevel getPermissionLevel() {
        return CommandLevel.OP_AFFECT_GAMEPLAY;
    }
    
    @Override
    public int compareTo(Object o) {
        return getCommandName().compareTo(((ICraftingCommand)o).getCommandName());
    }
}