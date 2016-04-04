package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.*;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.ICustomWidth;
import joshie.progression.api.special.IHasFilters;
import joshie.progression.api.special.ISpecialFieldProvider;
import joshie.progression.gui.fields.EntityFilterFieldPreview;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.gui.filters.FilterTypeEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@ProgressionRule(name="kill", color=0xFF000000)
public class TriggerKill extends TriggerBaseCounter implements ICustomWidth, IHasFilters, ISpecialFieldProvider {
    public List<IFilterProvider> entities = new ArrayList();

    @Override
    public ITrigger copy() {
        TriggerKill trigger = new TriggerKill();
        trigger.entities = entities;
        return copyCounter(trigger);
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.EDIT ? 100 : 85;
    }

    @Override
    public List<IFilterProvider> getAllFilters() {
        return entities;
    }

    @Override
    public IFilterType getFilterForField(String fieldName) {
        return FilterTypeEntity.INSTANCE;
    }

    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) {
            fields.add(new ItemFilterField("entities", this));
        } else fields.add(new EntityFilterFieldPreview("entities", this, 45, 70, 2.8F));
    }

    @SubscribeEvent
    public void onEvent(LivingDeathEvent event) {
        Entity source = event.source.getSourceOfDamage();
        if (source instanceof EntityPlayer) {
            ProgressionAPI.registry.fireTrigger((EntityPlayer) source, getProvider().getUnlocalisedName(), event.entityLiving);
        }
    }
    
    @Override
    protected boolean canIncrease(Object... data) {
        EntityLivingBase entity = (EntityLivingBase) data[0];
        for (IFilterProvider filter : entities) {
            if (filter.getProvided().matches(entity)) return true;
        }

        return false;
    }
}
