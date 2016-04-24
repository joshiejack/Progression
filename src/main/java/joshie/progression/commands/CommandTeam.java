package joshie.progression.commands;

import joshie.progression.helpers.PlayerHelper;
import joshie.progression.network.PacketHandler;
import joshie.progression.network.PacketInvitePlayer;
import joshie.progression.player.PlayerSavedData.TeamAction;
import joshie.progression.player.PlayerTeam;
import joshie.progression.player.PlayerTracker;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandTeam extends AbstractCommand {
    @Override
    public String getCommandName() {
        return "team";
    }

    @Override
    public String getUsage() {
        return "[action][name]";
    }

    @Override
    public boolean processCommand(ICommandSender sender, String[] parameters) {
        if (sender instanceof EntityPlayer) {
            EntityPlayer player = ((EntityPlayer)sender);
            if (parameters == null || parameters.length != 2) return false;
            else {
                String action = parameters[0];
                if (action == null) return false;
                if (action.equals("leave")) {
                    PlayerTracker.joinTeam(player, TeamAction.LEAVE, null, null);
                    return true;
                }

                String name = parameters[1];
                if (action == null || name == null) return false;
                if (action.equals("create")) {
                    PlayerTracker.joinTeam(player, TeamAction.NEW, null, name);
                } else if (action.equals("join")) {
                    PlayerTracker.joinTeam(player, TeamAction.JOIN, null, name);
                } else if (action.equals("invite")) {
                    PlayerTeam team = PlayerTracker.getPlayerData(player).getTeam();
                    if (team.isOwner(player)) {
                        for (EntityPlayerMP playerz : PlayerHelper.getAllPlayers()) {
                            if (playerz.getName().equalsIgnoreCase(name)) {
                                PacketHandler.sendToClient(new PacketInvitePlayer(team.getOwner(), team.getName(), name), playerz);
                                team.addToInvited(PlayerHelper.getUUIDForPlayer(playerz));
                                break;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }
}
