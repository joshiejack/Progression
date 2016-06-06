package joshie.progression.commands;

import joshie.progression.lib.CommandLevel;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Command
public class CommandHelp extends AbstractCommand {
    @Override
    public CommandLevel getPermissionLevel() {
        return CommandLevel.ANYONE;
    }

    @Override
    public String getCommandName() {
        return "help";
    }

    @Override
    public boolean processCommand(ICommandSender sender, String[] parameters) throws CommandException {
        List list = this.getSortedPossibleCommands(sender);
        byte b0 = 7;
        int i = (list.size() - 1) / b0;
        boolean flag = false;
        int k;

        try {
            k = parameters.length == 0 ? 0 : net.minecraft.command.CommandBase.parseInt(parameters[0], 1, i + 1) - 1;
        } catch (NumberInvalidException numberinvalidexception) {
            Map map = this.getCommands();
            AbstractCommand icommand = (AbstractCommand) map.get(parameters[0]);

            if (icommand != null) {
                CommandManager.throwError(sender, icommand);
                return true;
            }

            if (MathHelper.parseIntWithDefault(parameters[0], -1) != -1) {
                throw numberinvalidexception;
            }

            throw new CommandNotFoundException();
        }

        int j = Math.min((k + 1) * b0, list.size());
        ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation("crafting.commands.help.header", new Object[] { Integer.valueOf(k + 1), Integer.valueOf(i + 1) });
        chatcomponenttranslation1.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
        sender.addChatMessage(chatcomponenttranslation1);

        for (int l = k * b0; l < j; ++l) {
            AbstractCommand icommand1 = (AbstractCommand) list.get(l);
            ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(CommandManager.getUsage(icommand1), new Object[0]);
            chatcomponenttranslation.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + icommand1.getCommandName() + " "));
            sender.addChatMessage(chatcomponenttranslation);
        }

        return true;
    }

    private Map getCommands() {
        return CommandManager.INSTANCE.getCommands();
    }

    private List getSortedPossibleCommands(ICommandSender sender) {
        List list = CommandManager.INSTANCE.getPossibleCommands(sender);
        Collections.sort(list);
        return list;
    }

    @Override
    public String getUsage() {
        return "";
    }
}
