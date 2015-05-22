package joshie.progression.helpers;

import joshie.progression.Progression;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.Loader;

public class ModLogHelper {
    public static void log(String mod, String text) {
        if (Loader.isModLoaded(mod)) {
            Progression.logger.log(Level.INFO, text);
        }
    }
}
