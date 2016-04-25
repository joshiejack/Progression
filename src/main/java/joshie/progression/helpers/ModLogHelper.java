package joshie.progression.helpers;

import joshie.progression.Progression;
import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.Level;

public class ModLogHelper {
    public static void log(String mod, String text) {
        if (Loader.isModLoaded(mod)) {
            Progression.logger.log(Level.INFO, text);
        }
    }
}
