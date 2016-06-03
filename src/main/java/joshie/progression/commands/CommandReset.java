package joshie.progression.commands;

import joshie.progression.network.PacketHandler;
import joshie.progression.network.PacketReset;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

@Command
public class CommandReset extends AbstractCommand {
    @Override
    public String getCommandName() {
        return "reset";
    }

    @Override
    public String getUsage() {
        return "[username]";
    }

    @Override
    public boolean processCommand(ICommandSender sender, String[] parameters) {
        if (sender.getEntityWorld().isRemote) {
            if (parameters.length >= 1) PacketHandler.sendToServer(new PacketReset(parameters[0]));
            else PacketHandler.sendToServer(new PacketReset());
        } else if (sender instanceof EntityPlayer) {
            if (parameters.length >= 1) PacketReset.handle((EntityPlayer)sender, true, parameters[0]);
            else PacketReset.handle((EntityPlayer)sender, false, null);
        }

        return true;
    }
}
