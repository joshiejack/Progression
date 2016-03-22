package joshie.progression.commands;

import joshie.progression.Progression;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.lib.GuiIDs;
import joshie.progression.network.PacketHandler;
import joshie.progression.network.PacketOpenEditor;
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
        if (sender.getEntityWorld().isRemote) {
            MCClientHelper.getPlayer().openGui(Progression.instance, GuiIDs.EDITOR, null, 0, 0, 0);
        } else PacketHandler.sendToClient(new PacketOpenEditor(), sender);
        
        return true;
    }
}
