package joshie.progression.criteria.triggers;

import java.util.List;

import joshie.progression.api.IFilter;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.fields.IField;
import joshie.progression.api.fields.ISpecialFieldProvider;
import joshie.progression.gui.fields.ItemFilterFieldPreview;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TriggerItemEaten extends TriggerBaseItemFilter implements ISpecialFieldProvider {
    public TriggerItemEaten() {
        super("onEaten", 0xFF00B285);
    }

    @SubscribeEvent
    public void onEvent(PlayerUseItemEvent.Finish event) {
        ProgressionAPI.registry.fireTrigger(event.entityPlayer, getUnlocalisedName(), event.item);
    }
    
    @Override
    public boolean shouldReflectionSkipField(String name) {
        return name.equals("filters");
    }

    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) fields.add(ProgressionAPI.fields.getItemPreview(this, "filters", 30, 35, 1.9F));
    }

    @Override
    protected boolean canIncrease(Object... data) {
        ItemStack item = (ItemStack) data[0];
        for (IFilter filter : filters) {
            if (filter.matches((ItemStack) data[0])) return true;
        }

        return false;
    }
}
