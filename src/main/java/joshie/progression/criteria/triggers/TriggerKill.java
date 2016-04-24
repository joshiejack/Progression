package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.*;
import joshie.progression.api.special.*;
import joshie.progression.gui.fields.EntityFilterFieldPreview;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.gui.filters.FilterTypeEntity;
import joshie.progression.helpers.EntityHelper;
import joshie.progression.helpers.MCClientHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

import static joshie.progression.ItemProgression.ItemMeta.kill;
import static joshie.progression.ItemProgression.getStackFromMeta;

@ProgressionRule(name="kill", color=0xFF000000)
public class TriggerKill extends TriggerBaseCounter implements ICustomWidth, ICustomIcon, IMiniIcon, IAdditionalTooltip, IHasFilters, ISpecialFieldProvider {
    private static final ItemStack mini = getStackFromMeta(kill);
    public List<IFilterProvider> entities = new ArrayList();
    protected transient EntityLivingBase entity;
    protected transient int ticker;

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
    public ItemStack getIcon() {
        return EntityHelper.getItemForEntity(getEntity());
    }

    @Override
    public ItemStack getMiniIcon() {
        return mini;
    }

    @Override
    public void addHoverTooltip(String field, Object object, List tooltip) {
        tooltip.clear();
        if (entity != null) {
            tooltip.add(entity.getName());
        }
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

    @SubscribeEvent(priority = EventPriority.LOWEST)
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

    private EntityLivingBase getEntity() {
        if (ticker >= 200 || ticker == 0) {
            entity = EntityHelper.getRandomEntityFromFilters(entities, MCClientHelper.getPlayer());
            ticker = 1;
        }

        if (!GuiScreen.isShiftKeyDown()) ticker++;
        return entity != null ? entity : MCClientHelper.getPlayer();
    }
}
