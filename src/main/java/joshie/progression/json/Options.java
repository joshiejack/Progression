package joshie.progression.json;

import org.apache.logging.log4j.Level;

import joshie.progression.Progression;
import net.minecraftforge.common.config.Configuration;

public class Options {
    private static final String SETTINGS = "Settings";
    public static transient boolean debugMode;
    public static boolean editor = true;
    public static boolean tileClaimerRecipe;
    public static boolean overwriteCriteriaJSONForClients;
    public static boolean enableCriteriaBackups;
    public static int maximumCriteriaBackups;
    public static boolean mustClaimDefault;
    public static DefaultSettings settings;

    public static void init(Configuration config) {
        try {
            config.load();
            editor = config.get(SETTINGS, "Enable Editing", true).getBoolean(true);
            tileClaimerRecipe = config.get(SETTINGS, "Add Recipe for Tile Entity Claimer", true).getBoolean(true);
            overwriteCriteriaJSONForClients = config.getBoolean(SETTINGS, "Overwrite criteria.json", false,
                        "If this is true then Clients will always use the criteria.json file, and have it overridden by whatever is on a server, " +
                                "by default this is false, which means clients will create a new json file for every server they join, so that the data," +
                                "is cached instead of being recreated everytime they join a new server. This setting being false means that if you are editing" +
                                "criteria on a server, for editing a pack, then you need to give users the serverside criteria.json and not the one in your client folder");
            enableCriteriaBackups = config.getBoolean(SETTINGS, "Enable Criteria Backups", true, "Criteria will be backed up, whenever it's saved if this is true");
            maximumCriteriaBackups = config.getInt(SETTINGS, "Maximum Criteria Backups", 25, 1, 100, "This is the maximum number of backups to keep for criteria, maximum 100");
            mustClaimDefault = config.getBoolean(SETTINGS, "Default Setting for Claiming", false, "If this is true, new rewards will be set to mustClaim = true by default");
        } catch (Exception e) {
            Progression.logger.log(Level.ERROR, "Progression failed to load it's config");
            e.printStackTrace();
        } finally {
            config.save();
        }
    }
}
