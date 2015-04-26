package joshie.progression.commands;

import joshie.progression.Progression;
import joshie.progression.helpers.ClientHelper;
import net.minecraft.command.ICommandSender;

public class CommandEdit extends AbstractCommand {
    @Override
    public String getCommandName() {
        return "edit";
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public boolean processCommand(ICommandSender sender, String[] parameters) {
        ClientHelper.getPlayer().openGui(Progression.instance, 0, null, 0, 0, 0);
        return true;
    }
}
