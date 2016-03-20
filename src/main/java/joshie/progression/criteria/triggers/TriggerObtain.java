package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TriggerObtain extends TriggerBaseBoolean {
    public TriggerObtain() {
        super(new ItemStack(Blocks.chest), "openContainer", 0xFFFFFF00);
    }

    private boolean fired = false;
    private static Gui lastGui;

    @SubscribeEvent
    public void onEvent(GuiOpenEvent event) {
        if (lastGui != event.gui) {
            lastGui = event.gui;
            ProgressionAPI.registry.fireTriggerClientside(getUnlocalisedName());
        }
    }

    @Override
    protected boolean isTrue(Object... data) {
        return true;
    }
}
