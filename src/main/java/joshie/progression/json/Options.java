package joshie.progression.json;

import joshie.progression.Progression;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Level;

public class Options {
    public static boolean editor = true;
    public static boolean sync = true;
    public static String defaultTab = "DEFAULT";
    public static boolean requireUnlockForCrafting = false;
    public static boolean requireUnlockForUsage = false;
    
    public static boolean tileClaimerRecipe = false;
    public static boolean unclaimedTileCanUseAnythingForCrafting = false;
    public static boolean unclaimedTileCanCraftAnything = false;

    public static void init(Configuration config) {
        try {
            config.load();
            editor = config.get("Settings", "Enable Editing", true).getBoolean(true);
            sync = config.get("Settings", "Sync JSON from Server to Client", true).getBoolean(true);
            requireUnlockForCrafting = config.get("Settings", "Disable all 'Crafting' Recipes until Unlocked", false).getBoolean(false);
            requireUnlockForUsage = config.get("Settings", "Disable all 'Usage' Recipes until Unlocked", false).getBoolean(false);
            defaultTab = config.get("Settings", "Default Tab", "DEFAULT").getString();
            tileClaimerRecipe = config.get("Settings", "Add Recipe for Tile Entity Claimer", true).getBoolean(true);
            unclaimedTileCanUseAnythingForCrafting = config.get("Settings", "Unclaimed Tile Entities can use anything for crafting", false).getBoolean(false);
            unclaimedTileCanCraftAnything = config.get("Settings", "Unclaimed Tile Entities can craft any item", false).getBoolean(false);
        } catch (Exception e) {
            Progression.logger.log(Level.ERROR, "Progression failed to load it's config");
            e.printStackTrace();
        } finally {
            config.save();
        }
    }
}
