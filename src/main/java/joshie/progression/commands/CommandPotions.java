package joshie.progression.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.potion.Potion;

import java.io.PrintWriter;

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
            for(Potion potion: Potion.potionTypes) {
                if (potion != null) {
                    builder.append(potion.getName() + " ( " + potion.getId() +  ")\n");
                }
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