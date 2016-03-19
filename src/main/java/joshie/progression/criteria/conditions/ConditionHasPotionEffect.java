package joshie.progression.criteria.conditions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import joshie.progression.api.IFilter;
import joshie.progression.api.ISpecialFilters;
import joshie.progression.api.filters.IFilterSelectorFilter;
import joshie.progression.gui.selector.filters.PotionFilter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ConditionHasPotionEffect extends ConditionBase implements ISpecialFilters {
    public List<IFilter> filters = new ArrayList();

    public ConditionHasPotionEffect() {
        super("potioneffect", 0xFFFFFF00);
    }

    @Override
    public IFilterSelectorFilter getFilterForField(String fieldName) {
        return PotionFilter.INSTANCE;
    }

    @Override
    public boolean isSatisfied(World world, EntityPlayer player, UUID uuid) {
        for (IFilter filter : filters) {
            if (filter.matches(player.getActivePotionEffects())) return true;
        }

        return false;
    }
}
