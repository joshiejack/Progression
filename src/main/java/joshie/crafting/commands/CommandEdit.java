package joshie.crafting.commands;

import joshie.crafting.CraftingMod;
import joshie.crafting.helpers.ClientHelper;
import net.minecraft.command.ICommandSender;

public class CommandEdit extends CommandBase {
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
        ClientHelper.getPlayer().openGui(CraftingMod.instance, 0, null, 0, 0, 0);
        return true;
    }
}
