package joshie.progression.criteria.triggers;

import joshie.progression.api.criteria.IField;
import joshie.progression.api.criteria.IFilterProvider;
import joshie.progression.api.criteria.IFilterType;
import joshie.progression.api.special.*;
import joshie.progression.gui.fields.EntityFilterFieldPreview;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.gui.filters.FilterTypeEntity;
import joshie.progression.helpers.EntityHelper;
import joshie.progression.helpers.MCClientHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static joshie.progression.ItemProgression.ItemMeta.attackEntity;
import static joshie.progression.ItemProgression.getStackFromMeta;

public abstract class TriggerBaseEntity extends TriggerBaseCounter implements ICustomWidth, ICustomIcon, IMiniIcon, IHasFilters, ISpecialFieldProvider {
    private static final ItemStack mini = getStackFromMeta(attackEntity);
    public List<IFilterProvider> entities = new ArrayList();
    protected transient EntityLivingBase entity;
    protected transient int ticker;

    public TriggerBaseEntity copyEntity(TriggerBaseEntity trigger) {
        trigger.entities = entities;
        return (TriggerBaseEntity) copyCounter(trigger);
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.EDIT ? 100 : 85;
    }

    @Override
    public ItemStack getIcon() {
        return EntityHelper.getItemForEntity(getEntity());
    }

    @Override
    public ItemStack getMiniIcon() {
        return mini;
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

    @Override
    protected boolean canIncrease(Object... data) {
        EntityLivingBase entity = (EntityLivingBase) data[0];
        for (IFilterProvider filter : entities) {
            if (filter.getProvided().matches(entity)) return true;
        }

        return false;
    }

    private EntityLivingBase getEntity() {
        if (ticker >= 200 || ticker == 0) {
            entity = EntityHelper.getRandomEntityFromFilters(entities, MCClientHelper.getPlayer());
            ticker = 1;
        }

        if (!GuiScreen.isShiftKeyDown()) ticker++;
        return entity != null ? entity : MCClientHelper.getPlayer();
    }
}