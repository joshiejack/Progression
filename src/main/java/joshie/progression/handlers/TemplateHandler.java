package joshie.progression.handlers;

import joshie.progression.Progression;
import joshie.progression.helpers.FileHelper;
import joshie.progression.json.DataCriteria;
import joshie.progression.json.DataTab;
import joshie.progression.json.JSONLoader;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TemplateHandler  {
    private static List<DataTab> tabs = new ArrayList<DataTab>();
    private static List<DataCriteria> criterias = new ArrayList<DataCriteria>();

    public static boolean registerTab(DataTab tab) {
        if (tab == null) return false;
        tabs.add(tab);
        return true;
    }

    public static boolean registerCriteria(DataCriteria criteria) {
        if (criteria == null) return false;
        criterias.add(criteria);
        return true;
    }

    public static List<DataTab> getTabs() {
        return tabs;
    }

    public static List<DataCriteria> getCriteria() {
        return criterias;
    }

    public static void init() {
        //Load in the tabs
        Collection<File> files = FileUtils.listFiles(FileHelper.getTemplatesFolder("tab", null), new String[] { "json" }, false);
        for (File file : files) {
            try {
                String json = FileUtils.readFileToString(file);
                DataTab tab = JSONLoader.getGson().fromJson(json, DataTab.class);
                if (tab != null) {
                    Progression.logger.log(Level.INFO, "Loaded in the template for the criteria: " + tab.getName());
                    registerTab(tab);
                }


            } catch (Exception e) { e.printStackTrace(); }
        }

        //Load in the criteria
        //Load in the tabs
        files = FileUtils.listFiles(FileHelper.getTemplatesFolder("criteria", null), new String[] { "json" }, false);
        for (File file : files) {
            try {
                String json = FileUtils.readFileToString(file);
                DataCriteria criteria = JSONLoader.getGson().fromJson(json, DataCriteria.class);
                if (criteria != null) {
                    Progression.logger.log(Level.INFO, "Loaded in the template for the criteria: " + criteria.getName());
                    registerCriteria(criteria);
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}
