package joshie.progression.criteria.conditions;

import joshie.progression.Progression;
import joshie.progression.api.IPlayerTeam;
import joshie.progression.api.criteria.IField;
import joshie.progression.api.criteria.IFilter;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.ISpecialFieldProvider;
import joshie.progression.criteria.filters.FilterBase;
import joshie.progression.gui.fields.ItemFilterField;
import joshie.progression.items.ItemCriteria;
import joshie.progression.lib.WorldLocation;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class ConditionLocation extends ConditionBase implements ISpecialFieldProvider {
    public List<IFilter> locations = new ArrayList();
    protected transient IFilter locationpreview;
    protected transient int locationticker;

    public ConditionLocation() {
        super(ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.ifIsAtCoordinates), "location", 0xFF000000);
    }

    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) {
            fields.add(new ItemFilterField("locations", this));
        }
    }

    @Override
    public boolean isSatisfied(IPlayerTeam team) {
        for (EntityPlayer player: team.getTeamEntities()) {
            WorldLocation location = new WorldLocation(player);
            for (IFilter filter : locations) {
                if (filter.matches(location)) return true;
            }
        }

        return  false;
    }

    public String getFilter() {
        if (locationticker == 0 || locationticker >= 200) {
            locationpreview = FilterBase.getRandomFilterFromFilters(locations);
            locationticker = 1;
        }

        if (!GuiScreen.isShiftKeyDown()) locationticker++;

        return locationpreview == null ? "Nowhere": locationpreview.getDescription();
    }

    @Override
    public String getConditionDescription() {
        if (inverted) return Progression.translate(getUnlocalisedName() + ".description.inverted") + " \n" + getFilter();
        else return Progression.translate(getUnlocalisedName() + ".description") + " \n" + getFilter();
    }
}
