package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ITrigger;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.ICustomDescription;
import joshie.progression.api.special.ICustomWidth;
import joshie.progression.helpers.ChatHelper;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@ProgressionRule(name="openContainer", color=0xFFFFFF00, meta="onGUIChange")
public class TriggerChangeGui extends TriggerBaseBoolean implements ICustomDescription, ICustomWidth {
    private static boolean DEBUG = false;
    @SideOnly(Side.CLIENT)
    private static Gui lastGui;

    public String className = "joshie.progression.gui.core.GuiCore";
    public String description = "Open the Progression Book";
    public int displayWidth = 75;

    @Override
    public ITrigger copy() {
        TriggerChangeGui trigger = new TriggerChangeGui();
        trigger.className = className;
        trigger.description = description;
        trigger.displayWidth = displayWidth;
        return copyBoolean(trigger);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.EDIT ? 100 : displayWidth;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onEvent(GuiOpenEvent event) {
        if (lastGui != event.getGui()) {
            lastGui = event.getGui();
            if (event.getGui() == null) return; //NO NULLS!
            ProgressionAPI.registry.fireTriggerClientside(getProvider().getUnlocalisedName(), event.getGui().getClass().getCanonicalName().toString());
        }

        //If debuger is enabled, display the class name for the gui
        if (DEBUG) {
            if (event.getGui() == null) return; //NO NULLS!
            ChatHelper.displayChatAndLog(event.getGui().getClass().getCanonicalName().toString());
        }
    }

    public static boolean toggleDebug() {
        DEBUG = !DEBUG;
        return DEBUG;
    }

    @Override
    protected boolean isTrue(Object... data) {
        String name = (String) data[0];
        return name.equals(className);
    }
}
