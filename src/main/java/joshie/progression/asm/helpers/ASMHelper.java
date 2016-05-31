package joshie.progression.asm.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import joshie.progression.asm.ASMConfig;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import static joshie.progression.lib.PInfo.MODID;

public class ASMHelper {
    public static ASMConfig getASMConfig() {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        File file = new File("config/" + MODID + "/asm.json");
        if (!file.exists()) {
            try {
                ASMConfig config = new ASMConfig();
                Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
                writer.write(gson.toJson(config));
                writer.close(); //Write the default json to file
                return config;
            } catch (Exception ignored) {}
        } else {
            try {
                return gson.fromJson(FileUtils.readFileToString(file), ASMConfig.class);
            } catch (Exception ignored) {}
        }

        return new ASMConfig();
    }
}
