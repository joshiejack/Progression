package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IField;
import joshie.progression.api.criteria.IFilter;
import joshie.progression.api.criteria.ITrigger;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.ISpecialFieldProvider;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class TriggerItemEaten extends TriggerBaseItemFilter implements ISpecialFieldProvider {
    public TriggerItemEaten() {
        super("onEaten", 0xFF00B285);
    }

    @Override
    public ITrigger copy() {
        TriggerItemEaten trigger = new TriggerItemEaten();
        return copyBase(copyCounter(copyFilter(trigger)));
    }

    @SubscribeEvent
    public void onEvent(PlayerUseItemEvent.Finish event) {
        ProgressionAPI.registry.fireTrigger(event.entityPlayer, getUnlocalisedName(), event.item);
    }
    
    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) fields.add(ProgressionAPI.fields.getItemPreview(this, "filters", 30, 35, 1.9F));
        else fields.add(ProgressionAPI.fields.getItemPreview(this, "filters", 65, 35, 1.9F));
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
