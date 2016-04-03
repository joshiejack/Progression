package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IField;
import joshie.progression.api.criteria.IFilter;
import joshie.progression.api.criteria.IFilterType;
import joshie.progression.api.criteria.ITrigger;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.IHasFilters;
import joshie.progression.api.special.ISpecialFieldProvider;
import joshie.progression.api.special.ISpecialFilters;
import joshie.progression.gui.fields.EntityFilterFieldPreview;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.gui.filters.FilterTypeEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class TriggerKill extends TriggerBaseCounter implements IHasFilters, ISpecialFilters, ISpecialFieldProvider {
    public List<IFilter> entities = new ArrayList();

    public TriggerKill() {
        super(new ItemStack(Items.iron_sword), "kill", 0xFF000000);
    }

    @Override
    public ITrigger copy() {
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
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) {
            fields.add(new ItemFilterField("entities", this));
        } else fields.add(new EntityFilterFieldPreview("entities", this, 45, 70, 2.8F));
    }
    
    @Override
    public IFilterType getFilterForField(String fieldName) {
        return FilterTypeEntity.INSTANCE;
    }

    @Override
    public List<IFilter> getAllFilters() {
        return entities;
    }

    @Override
    protected boolean canIncrease(Object... data) {
        EntityLivingBase entity = (EntityLivingBase) data[0];
        for (IFilter filter : entities) {
            if (filter.matches(entity)) return true;
        }

        return false;
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.EDIT ? super.getWidth(mode) : 85;
    }
}
