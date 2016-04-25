package joshie.progression.json;

import joshie.progression.Progression;
import joshie.progression.lib.PInfo;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;

import java.io.File;

public class Options {
    public static Configuration config;
    public static final String SETTINGS = "Settings";
    public static transient boolean debugMode;
    public static boolean editor = true;
    public static boolean tileClaimerRecipe;
    public static boolean overwriteCriteriaJSONForClients;
    public static boolean enableCriteriaBackups;
    public static int maximumCriteriaBackups;
    public static boolean mustClaimDefault;
    public static boolean hardReset;
    public static DefaultSettings settings;

    public static void init(File configFile) {
        if (config == null) {
            config = new Configuration(configFile);
            loadConfiguration();
        }
        MinecraftForge.EVENT_BUS.register(new Options());
    }

    private static void loadConfiguration() {
        try {
            editor = config.get(SETTINGS, "Enable Editing", true).getBoolean();
            tileClaimerRecipe = config.get(SETTINGS, "Add Recipe for Tile Entity Claimer", true).getBoolean();
            overwriteCriteriaJSONForClients = config.get(SETTINGS, "Overwrite criteria.json", false,
                    "If this is true then Clients will always use the criteria.json file, and have it overridden by whatever is on a server, " +
                            "by default this is false, which means clients will create a new json file for every server they join, so that the data," +
                            "is cached instead of being recreated everytime they join a new server. This setting being false means that if you are editing" +
                            "criteria on a server, for editing a pack, then you need to give users the serverside criteria.json and not the one in your client folder").getBoolean();
            enableCriteriaBackups = config.get(SETTINGS, "Enable Criteria Backups", true, "Criteria will be backed up, whenever it's saved if this is true").getBoolean();
            maximumCriteriaBackups = config.get(SETTINGS, "Maximum Criteria Backups", 25, "This is the maximum number of backups to keep for criteria, maximum 100", 1, 100).getInt();
            mustClaimDefault = config.get(SETTINGS, "Default Setting for Claiming", false, "If this is true, new rewards will be set to mustClaim = true by default").getBoolean();
            hardReset = config.get(SETTINGS, "Remove Players from Teams when Resetting Data", false, "When this is true, players will be removed from their teams when you execute the progression reset command").getBoolean();
        } catch (Exception e) {
            Progression.logger.log(Level.ERROR, "Progression failed to load it's config");
            e.printStackTrace();
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equalsIgnoreCase(PInfo.MODID)) {
            loadConfiguration();
        }
    }
}