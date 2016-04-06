package joshie.progression.helpers;

import joshie.progression.json.Options;

import java.io.File;

public class FileHelper {
    public static File root;

    public static File getCriteriaFile(String serverName, boolean isClient) {
        if (!isClient || (isClient && Options.overwriteCriteriaJSONForClients) || serverName.equals("ssp")) return new File(getRoot(), "criteria.json");
        else { //Borrowed from Psi by Vazkii
            String home = System.getProperty("user.home");
            String os = System.getProperty("os.name");
            if(os.startsWith("Windows")) home += "\\AppData\\Roaming\\.minecraft\\progression_servers";
            else if(os.startsWith("Mac")) home += "/Library/Application Support/minecraft/progression_servers";
            else home += "/.minecraft/progression_servers";

            File dir = new File(home);
            if(!dir.exists()) dir.mkdirs();
            return new File(home, serverName + ".json");
        }
    }

    public static File getOptions() {
        return new File(getRoot(), "options.cfg");
    }

    public static File getRoot() {
        if (!root.exists()) root.mkdir();

        return root;
    }
}
