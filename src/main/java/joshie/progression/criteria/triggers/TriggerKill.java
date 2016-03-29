package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IProgressionField;
import joshie.progression.api.criteria.IProgressionFilter;
import joshie.progression.api.criteria.IProgressionFilterSelector;
import joshie.progression.api.criteria.IProgressionTrigger;
import joshie.progression.api.special.IHasFilters;
import joshie.progression.api.special.ISpecialFieldProvider;
import joshie.progression.api.special.ISpecialFilters;
import joshie.progression.gui.fields.EntityFilterFieldPreview;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.gui.filters.FilterSelectorEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class TriggerKill extends TriggerBaseCounter implements IHasFilters, ISpecialFilters, ISpecialFieldProvider {
    public List<IProgressionFilter> entities = new ArrayList();

    public TriggerKill() {
        super(new ItemStack(Items.iron_sword), "kill", 0xFF000000);
    }

    @Override
    public IProgressionTrigger copy() {
        TriggerKill trigger = new TriggerKill();
        trigger.entities = entities;
        return copyBase(copyCounter(trigger));
    }

    @SubscribeEvent
    public void onEvent(LivingDeathEvent event) {
        Entity source = event.source.getSourceOfDamage();
        if (source instanceof EntityPlayer) {
            ProgressionAPI.registry.fireTrigger((EntityPlayer) source, getUnlocalisedName(), event.entityLiving);
        }
    }

    @Override
    public void addSpecialFields(List<IProgressionField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) {
            fields.add(new ItemFilterField("entities", this));
        } else fields.add(new EntityFilterFieldPreview("entities", this, 45, 70, 2.8F));
    }
    
    @Override
    public IProgressionFilterSelector getFilterForField(String fieldName) {
        return FilterSelectorEntity.INSTANCE;
    }

    @Override
    public List<IProgressionFilter> getAllFilters() {
        return entities;
    }

    @Override
    protected boolean canIncrease(Object... data) {
        EntityLivingBase entity = (EntityLivingBase) data[0];
        for (IProgressionFilter filter : entities) {
            if (filter.matches(entity)) return true;
        }

        return false;
    }

    @Override
    public int getWidth() {
        return 85;
    }

    @Override
    public String getDescription() {
        return StatCollector.translateToLocalFormatted("Kill %s x any of these entities\n\n%s%% completed", amount, getPercentage());
    }
}
