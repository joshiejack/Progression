package joshie.progression.criteria.conditions;

import joshie.progression.Progression;
import joshie.progression.api.IPlayerTeam;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.*;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.ICustomDescription;
import joshie.progression.api.special.IHasFilters;
import joshie.progression.api.special.ISpecialFieldProvider;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.lib.WorldLocation;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

@ProgressionRule(name="location", color=0xFF111111, meta="ifIsAtCoordinates")
public class ConditionLocation extends ConditionBase implements ICustomDescription, IHasFilters, ISpecialFieldProvider {
    public List<IFilterProvider> locations = new ArrayList();
    protected transient IField field;

    public ConditionLocation() {
        field = new ItemFilterField("locations", this);
    }

    @Override
    public String getDescription() {
        if (getProvider().isInverted()) return Progression.translate(getProvider().getUnlocalisedName() + ".description.inverted") + " \n" + field.getField();
        else return Progression.translate(getProvider().getUnlocalisedName() + ".description") + " \n" + field.getField();
    }

    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) {
            fields.add(new ItemFilterField("locations", this));
        }
    }

    @Override
    public List<IFilterProvider> getAllFilters() {
        return locations;
    }

    @Override
    public IFilterType getFilterForField(String fieldName) {
        return ProgressionAPI.filters.getLocationFilter();
    }

    @Override
    public boolean isSatisfied(IPlayerTeam team) {
        for (EntityPlayer player: team.getTeamEntities()) {
            WorldLocation location = new WorldLocation(player);
            for (IFilterProvider filter : locations) {
                if (filter.getProvided().matches(location)) return true;
            }
        }

        return  false;
    }
}
