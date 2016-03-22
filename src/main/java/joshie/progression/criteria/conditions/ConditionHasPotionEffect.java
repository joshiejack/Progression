package joshie.progression.criteria.conditions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import joshie.progression.api.criteria.IProgressionFilter;
import joshie.progression.api.criteria.IProgressionFilterSelector;
import joshie.progression.api.special.ISpecialFilters;
import joshie.progression.gui.filters.FilterSelectorPotion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ConditionHasPotionEffect extends ConditionBase implements ISpecialFilters {
    public List<IProgressionFilter> filters = new ArrayList();

    public ConditionHasPotionEffect() {
        super("potioneffect", 0xFFFFFF00);
    }

    @Override
    public IProgressionFilterSelector getFilterForField(String fieldName) {
        return FilterSelectorPotion.INSTANCE;
    }

    @Override
    public boolean isSatisfied(World world, EntityPlayer player, UUID uuid) {
        for (IProgressionFilter filter : filters) {
            if (filter.matches(player.getActivePotionEffects())) return true;
        }

        return false;
    }
}
