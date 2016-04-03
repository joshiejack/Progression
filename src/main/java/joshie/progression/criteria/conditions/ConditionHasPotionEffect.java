package joshie.progression.criteria.conditions;

import joshie.progression.api.IPlayerTeam;
import joshie.progression.api.criteria.IField;
import joshie.progression.api.criteria.IFilter;
import joshie.progression.api.criteria.IFilterType;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.ISpecialFieldProvider;
import joshie.progression.api.special.ISpecialFilters;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.gui.filters.FilterTypePotion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;

import java.util.List;

public class ConditionHasPotionEffect extends ConditionBaseItemFilter implements ISpecialFilters, ISpecialFieldProvider {
    public String description = "Have the regeneration potion effect";
    public int lessThanFalse = 220;

    public ConditionHasPotionEffect() {
        super("potioneffect", 0xFFFFFF00);
    }

    @Override
    public IFilterType getFilterForField(String fieldName) {
        return FilterTypePotion.INSTANCE;
    }

    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) {
            fields.add(new ItemFilterField("filters", this));
        }
    }

    @Override
    public boolean isSatisfied(IPlayerTeam team) {
        for (EntityPlayer player: team.getTeamEntities()) {
            for (PotionEffect effect: player.getActivePotionEffects()) {
                if (effect.getDuration() > lessThanFalse) {
                    for (IFilter filter : filters) {
                        if (filter.matches(effect)) return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public String getConditionDescription() {
        return description;
    }
}
