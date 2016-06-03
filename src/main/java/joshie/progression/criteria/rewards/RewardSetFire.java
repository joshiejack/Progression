package joshie.progression.criteria.rewards;

import joshie.progression.api.criteria.*;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.IHasFilters;
import joshie.progression.api.special.ISpecialFieldProvider;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.gui.filters.FilterTypeEntity;
import joshie.progression.helpers.EntityHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.ArrayList;
import java.util.List;

@ProgressionRule(name="setFire", color=0xFFFF4200, meta="setFire")
public class RewardSetFire extends RewardBase implements IHasFilters, ISpecialFieldProvider {
    public List<IFilterProvider> targets = new ArrayList();
    public boolean defaultToPlayer = true;
    public int duration;

    @Override
    public List<IFilterProvider> getAllFilters() {
        return targets;
    }

    @Override
    public IFilterType getFilterForField(String fieldName) {
        return FilterTypeEntity.INSTANCE;
    }

    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        fields.add(new ItemFilterField("targets", this));
    }

    @Override
    public void reward(EntityPlayerMP thePlayer) {
        IFilter filter = EntityHelper.getFilter(targets, thePlayer);
        if (filter != null) {
            List<EntityLivingBase> entities = (List<EntityLivingBase>) filter.getRandom(thePlayer);
            if (entities.size() == 0) entities.add(thePlayer);
            if (entities.size() == 0 && defaultToPlayer) entities.add(thePlayer);
            for (EntityLivingBase entity : entities) {
                entity.setFire(duration);
            }
        }
    }
}