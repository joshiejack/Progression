package joshie.progression.criteria.triggers;

import java.util.ArrayList;
import java.util.List;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IProgressionFilter;
import joshie.progression.api.criteria.IProgressionFilterSelector;
import joshie.progression.api.special.IHasFilters;
import joshie.progression.api.special.ISpecialFilters;
import joshie.progression.gui.filters.FilterSelectorEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TriggerKill extends TriggerBaseCounterVaries implements IHasFilters, ISpecialFilters {
    public List<IProgressionFilter> entities = new ArrayList();

    public TriggerKill() {
        super(new ItemStack(Items.iron_sword), "kill", 0xFF000000);
    }

    @SubscribeEvent
    public void onEvent(LivingDeathEvent event) {
        Entity source = event.source.getSourceOfDamage();
        if (source instanceof EntityPlayer && source instanceof EntityLivingBase) {
            ProgressionAPI.registry.fireTrigger((EntityPlayer) source, getUnlocalisedName(), event.entityLiving);
        }
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
    public String getDescription() {
        String name = "INVALID";
        try {
            //name = entity.getName();
        } catch (Exception e) {}

        return amount + " x " + name;
    }
}
