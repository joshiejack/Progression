package joshie.progression.json;

import org.apache.logging.log4j.Level;

import joshie.progression.Progression;
import net.minecraftforge.common.config.Configuration;

public class Options {
	public static transient boolean debugMode = true;
    public static boolean editor = true;
    public static boolean tileClaimerRecipe = false;
    public static boolean overwriteCriteriaJSONForClients = false;
    public static DefaultSettings settings;

    public static void init(Configuration config) {
        try {
            config.load();
            editor = config.get("Settings", "Enable Editing", true).getBoolean(true);
            tileClaimerRecipe = config.get("Settings", "Add Recipe for Tile Entity Claimer", true).getBoolean(true);
            overwriteCriteriaJSONForClients = config.getBoolean("Settings", "Overwrite criteria.json", false,
                        "If this is true then Clients will always use the criteria.json file, and have it overridden by whatever is on a server, " +
                                "by default this is false, which means clients will create a new json file for every server they join, so that the data," +
                                "is cached instead of being recreated everytime they join a new server. This setting being false means that if you are editing" +
                                "criteria on a server, for editing a pack, then you need to give users the serverside criteria.json and not the one in your client folder");
        } catch (Exception e) {
            Progression.logger.log(Level.ERROR, "Progression failed to load it's config");
            e.printStackTrace();
        } finally {
            config.save();
        }
    }
}
