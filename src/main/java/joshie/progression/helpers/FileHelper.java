package joshie.progression.helpers;

import joshie.progression.json.Options;

import java.io.File;
import java.util.UUID;

public class FileHelper {
    public static File root;

    public static File getTemplatesFolder(String dir, UUID uuid) {
        File file = new File(getRoot(), "templates");
        if (!file.exists()) file.mkdir();
        file = new File(file, dir);
        if (!file.exists()) file.mkdir();
        if (uuid == null) return file;
        return new File(file, uuid.toString() + ".json");
    }

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

    public static File getBackupFile(String serverName, boolean isClient) {
        if (!isClient || (isClient && Options.overwriteCriteriaJSONForClients) || serverName.equals("ssp")) return new File(getBackup(), "criteria_" + System.currentTimeMillis() + ".json");
        else { //Borrowed from Psi by Vazkii
            String home = System.getProperty("user.home");
            String os = System.getProperty("os.name");
            if(os.startsWith("Windows")) home += "\\AppData\\Roaming\\.minecraft\\progression_servers\\backup";
            else if(os.startsWith("Mac")) home += "/Library/Application Support/minecraft/progression_servers/backup";
            else home += "/.minecraft/progression_servers/backup";

            File dir = new File(home);
            if(!dir.exists()) dir.mkdirs();
            return new File(home, serverName + ".json");
        }
    }

    public static File getBackup() {
        File file = new File(getRoot(), "backup");
        if (!file.exists()) file.mkdir();
        return file;
    }

    public static File getOptions() {
        return new File(getRoot(), "options.cfg");
    }

    public static File getRoot() {
        if (!root.exists()) root.mkdir();

        return root;
    }
}
