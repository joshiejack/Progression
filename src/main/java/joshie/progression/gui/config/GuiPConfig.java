package joshie.progression.gui.config;

import joshie.progression.json.Options;
import joshie.progression.lib.PInfo;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;

public class GuiPConfig extends GuiConfig {
    public GuiPConfig(GuiScreen parentScreen) {
        super(parentScreen, new ConfigElement(Options.config.getCategory(Options.SETTINGS.toLowerCase())).getChildElements(), PInfo.MODID, false, false, ".minecraft/config/progression/options.cfg");
    }
}