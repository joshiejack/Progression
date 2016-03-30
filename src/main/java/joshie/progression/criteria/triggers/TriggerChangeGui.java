package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IProgressionTrigger;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.helpers.ChatHelper;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TriggerChangeGui extends TriggerBaseBoolean {
    private static boolean DEBUG = false;
    public String className = "joshie.progression.gui.core.GuiCore";
    public String description = "Open the Progression Book";
    public int displayWidth = 75;

    public TriggerChangeGui() {
        super(new ItemStack(Blocks.chest), "openContainer", 0xFFFFFF00);
    }

    @Override
    public IProgressionTrigger copy() {
        TriggerChangeGui trigger = new TriggerChangeGui();
        trigger.className = className;
        trigger.description = description;
        trigger.displayWidth = displayWidth;
        return copyBase(copyBoolean(trigger));
    }

    private static Gui lastGui;

    @SubscribeEvent
    public void onEvent(GuiOpenEvent event) {
        if (lastGui != event.gui) {
            lastGui = event.gui;
            if (event.gui == null) return; //NO NULLS!
            ProgressionAPI.registry.fireTriggerClientside(getUnlocalisedName(), event.gui.getClass().getCanonicalName().toString());
        }

        //If debuger is enabled, display the class name for the gui
        if (DEBUG) {
            if (event.gui == null) return; //NO NULLS!
            ChatHelper.displayChatAndLog(event.gui.getClass().getCanonicalName().toString());
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

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.EDIT ? super.getWidth(mode) : displayWidth;
    }

    @Override
    public String getTriggerDescription() {
        return description;
    }
}
