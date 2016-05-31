package joshie.progression.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;

import java.io.PrintWriter;

public class CommandAchievement extends AbstractCommand {
    @Override
    public String getCommandName() {
        return "achievements";
    }

    @Override
    public boolean processCommand(ICommandSender sender, String[] parameters) {
        try {
            StringBuilder builder = new StringBuilder();
            for (Achievement a: AchievementList.ACHIEVEMENTS) {
                if (a == null) continue;
                builder.append(a.statId + "\n");
            }

            PrintWriter writer = new PrintWriter("achievements.log", "UTF-8");
            writer.write(builder.toString());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}
