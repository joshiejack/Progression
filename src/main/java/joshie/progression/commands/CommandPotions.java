package joshie.progression.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.potion.Potion;

import java.io.PrintWriter;
import java.util.Iterator;

@Command
public class CommandPotions extends AbstractCommand {
    @Override
    public String getCommandName() {
        return "potions";
    }

    @Override
    public boolean processCommand(ICommandSender sender, String[] parameters) {
        try {
            StringBuilder builder = new StringBuilder();
            Iterator<Potion> it = Potion.REGISTRY.iterator();
            while (it.hasNext()) {
                builder.append(it.next().getRegistryName() + "\n");
            }

            PrintWriter writer = new PrintWriter("potions.log", "UTF-8");
            writer.write(builder.toString());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}