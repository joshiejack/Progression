package joshie.progression.helpers;

import joshie.progression.Progression;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;
import org.apache.logging.log4j.Level;

public class ChatHelper {
    /** Borrowed from Blood Magic by WayofTime **/
    private static final int DELETION_ID = 2525277;
    private static int lastAdded;

    private static void displayChatMessages(IChatComponent[] messages) {
        GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        for (int i = DELETION_ID + messages.length - 1; i <= lastAdded; i++) {
            chat.deleteChatLine(i);
        }

        for (int i = 0; i < messages.length; i++) {
            chat.printChatMessageWithOptionalDeletion(messages[i], DELETION_ID + i);
        }

        lastAdded = DELETION_ID + messages.length - 1;
    }

    private static IChatComponent[] wrap(String... unlocalised) {
        String[] localised = localise(unlocalised);
        IChatComponent[] ret = new IChatComponent[localised.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = wrap(localised[i]);
        }

        return ret;
    }

    private static String[] localise(String... strings) {
        String[] newstrings = new String[strings.length];
        for (int i = 0; i < newstrings.length; i++) {
            if (strings[i].startsWith("progression.")) newstrings[i] = StatCollector.translateToLocal(strings[i]);
            else newstrings[i] = strings[i];
        }

        return newstrings;
    }

    private static IChatComponent wrap(String s) {
        return new ChatComponentTranslation(s);
    }

    public static void displayChat(String... strings) {
        displayChatMessages(wrap(strings));
    }

    public static void displayChatAndLog(String... strings) {
        displayChat(strings); //YO DISPLAY YO CHAT
        for (String string: strings) Progression.logger.log(Level.INFO, string);
    }
}
