package joshie.crafting.json;

import joshie.crafting.CraftingMod;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Level;

public class Options {
    public static boolean editor = true;
    public static boolean sync = true;
    public static String defaultTab = "DEFAULT";

    public static void init(Configuration config) {
        try {
            config.load();
            editor = config.get("Settings", "Enable Editing", true).getBoolean(true);
            sync = config.get("Settings", "Sync JSON from Server to Client", true).getBoolean(true);
            defaultTab = config.get("Settings", "Default Tab", "DEFAULT").getString();
        } catch (Exception e) {
            CraftingMod.logger.log(Level.ERROR, "Progression failed to load it's config");
            e.printStackTrace();
        } finally {
            config.save();
        }
    }
}
