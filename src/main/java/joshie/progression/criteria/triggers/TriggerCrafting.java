package joshie.progression.criteria.triggers;

import java.util.List;
import java.util.UUID;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IProgressionField;
import joshie.progression.api.criteria.IProgressionFilter;
import joshie.progression.api.criteria.IProgressionTriggerData;
import joshie.progression.api.special.ISpecialFieldProvider;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class TriggerCrafting extends TriggerBaseItemFilter implements ISpecialFieldProvider {
    public int timesCrafted = 1;

    public TriggerCrafting() {
        super("crafting", 0xFF663300, "crafting");
    }

    @SubscribeEvent
    public void onEvent(ItemCraftedEvent event) {
        ProgressionAPI.registry.fireTrigger(event.player, getUnlocalisedName(), event.crafting.copy());
    }
    
    @Override
    public boolean shouldReflectionSkipField(String name) {
        return name.equals("filters");
    }
    
    @Override
    public void addSpecialFields(List<IProgressionField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) fields.add(ProgressionAPI.fields.getItemPreview(this, "filters", 30, 35, 1.9F));
    }

    @Override
    public boolean isCompleted(IProgressionTriggerData existing) {
        int craftedNumber = ProgressionAPI.data.getDualNumber1(existing);
        int timesNumber = ProgressionAPI.data.getDualNumber2(existing);
        return craftedNumber >= amount && timesNumber >= timesCrafted;
    }

    @Override
    public boolean onFired(UUID uuid, IProgressionTriggerData existing, Object... additional) {
        ItemStack crafted = (ItemStack) (additional[0]);
        for (IProgressionFilter filter : filters) {
            if (filter.matches(crafted)) {
                int craftedNumber = ProgressionAPI.data.getDualNumber1(existing) + crafted.stackSize;
                int timesNumber = ProgressionAPI.data.getDualNumber2(existing) + 1;
                ProgressionAPI.data.setDualData(existing, craftedNumber, timesNumber);
                return true;
            }
        }

        return true;
    }
}
