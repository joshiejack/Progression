package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IField;
import joshie.progression.api.criteria.IFilterProvider;
import joshie.progression.api.criteria.ITrigger;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.IMiniIcon;
import joshie.progression.api.special.ISpecialFieldProvider;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

import static joshie.progression.ItemProgression.ItemMeta.eat;
import static joshie.progression.ItemProgression.getStackFromMeta;

@ProgressionRule(name="onEaten", color=0xFF00B285)
public class TriggerItemEaten extends TriggerBaseItemFilter implements IMiniIcon, ISpecialFieldProvider {
    private static final ItemStack mini = getStackFromMeta(eat);

    @Override
    public ITrigger copy() {
        return copyCounter(copyFilter(new TriggerItemEaten()));
    }

    @Override
    public ItemStack getMiniIcon() {
        return mini;
    }

    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) fields.add(ProgressionAPI.fields.getItemPreview(this, "filters", 30, 35, 1.9F));
        else fields.add(ProgressionAPI.fields.getItemPreview(this, "filters", 65, 35, 1.9F));
    }

    @SubscribeEvent
    public void onEvent(PlayerUseItemEvent.Finish event) {
        ProgressionAPI.registry.fireTrigger(event.entityPlayer, getProvider().getUnlocalisedName(), event.item);
    }

    @Override
    protected boolean canIncrease(Object... data) {
        ItemStack item = (ItemStack) data[0];
        for (IFilterProvider filter : filters) {
            if (filter.getProvided().matches((ItemStack) data[0])) return true;
        }

        return false;
    }
}
