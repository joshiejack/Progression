package joshie.progression.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityList;

import java.io.PrintWriter;

@Command
public class CommandEntity extends AbstractCommand {
    @Override
    public String getCommandName() {
        return "entities";
    }

    @Override
    public boolean processCommand(ICommandSender sender, String[] parameters) {
        try {
            StringBuilder builder = new StringBuilder();
            for (String s: EntityList.NAME_TO_CLASS.keySet()) {
                if (s == null) continue;
                builder.append(s + "\n");
            }

            PrintWriter writer = new PrintWriter("entities.log", "UTF-8");
            writer.write(builder.toString());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}