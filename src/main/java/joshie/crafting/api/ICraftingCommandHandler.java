package joshie.crafting.api;

import java.util.List;
import java.util.Map;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

public interface ICraftingCommandHandler extends ICommand {
    public void registerCommand(ICraftingCommand command);

    public Map getCommands();

    public List getPossibleCommands(ICommandSender sender);
}