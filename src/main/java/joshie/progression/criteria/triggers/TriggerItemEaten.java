package joshie.progression.criteria.triggers;

import java.util.ArrayList;
import java.util.List;

import joshie.progression.api.IField;
import joshie.progression.api.IItemFilter;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.gui.ISpecialFieldProvider;
import joshie.progression.gui.fields.ItemFilterFieldPreview;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TriggerItemEaten extends TriggerBaseCounter implements ISpecialFieldProvider {
    public List<IItemFilter> filters = new ArrayList();

    public TriggerItemEaten() {
        super("onEaten", 0xFF00B285);
    }

    @SubscribeEvent
    public void onEvent(PlayerUseItemEvent.Finish event) {
        ProgressionAPI.registry.fireTrigger(event.entityPlayer, getUnlocalisedName(), event.item);
    }

    @Override
    public void addSpecialFields(List<IField> fields) {
        fields.add(new ItemFilterFieldPreview("filters", this, 25, 30, 26, 70, 25, 75, 2.8F));
    }

    @Override
    protected boolean canIncrease(Object... data) {
        ItemStack item = (ItemStack) data[0];
        for (IItemFilter filter : filters) {
            if (filter.matches((ItemStack) data[0])) return true;
        }

        return false;
    }
}
