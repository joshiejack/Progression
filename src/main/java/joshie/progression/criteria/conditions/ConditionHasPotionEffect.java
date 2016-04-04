package joshie.progression.criteria.conditions;

import joshie.progression.api.IPlayerTeam;
import joshie.progression.api.criteria.*;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.ICustomDescription;
import joshie.progression.api.special.ISpecialFieldProvider;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.gui.filters.FilterTypePotion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;

import java.util.List;

@ProgressionRule(name="potioneffect", color=0xFFFFFF00)
public class ConditionHasPotionEffect extends ConditionBaseItemFilter implements ICustomDescription, ISpecialFieldProvider {
    public String description = "Have the regeneration potion effect";
    public int lessThanFalse = 220;

    @Override
    public String getDescription() {
        return description;
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
                    for (IFilterProvider filter : filters) {
                        if (filter.getProvided().matches(effect)) return true;
                    }
                }
            }
        }

        return false;
    }
}
