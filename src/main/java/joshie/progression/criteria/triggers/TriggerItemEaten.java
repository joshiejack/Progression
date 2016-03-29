package joshie.progression.criteria.triggers;

import joshie.progression.Progression;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IProgressionField;
import joshie.progression.api.criteria.IProgressionFilter;
import joshie.progression.api.criteria.IProgressionTrigger;
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
    public IProgressionTrigger copy() {
        TriggerItemEaten trigger = new TriggerItemEaten();
        return copyBase(copyCounter(copyFilter(trigger)));
    }

    @SubscribeEvent
    public void onEvent(PlayerUseItemEvent.Finish event) {
        ProgressionAPI.registry.fireTrigger(event.entityPlayer, getUnlocalisedName(), event.item);
    }
    
    @Override
    public void addSpecialFields(List<IProgressionField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) fields.add(ProgressionAPI.fields.getItemPreview(this, "filters", 30, 35, 1.9F));
        else fields.add(ProgressionAPI.fields.getItemPreview(this, "filters", 65, 35, 1.9F));
    }

    @Override
    protected boolean canIncrease(Object... data) {
        ItemStack item = (ItemStack) data[0];
        for (IProgressionFilter filter : filters) {
            if (filter.matches((ItemStack) data[0])) return true;
        }

        return false;
    }

    @Override
    public String getDescription() {
        return Progression.format("trigger.onEaten.description", amount) + "\n\n" + Progression.format("completed", getPercentage());
    }
}
