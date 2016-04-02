package joshie.progression.criteria.filters;

import joshie.progression.Progression;
import joshie.progression.api.criteria.IProgressionFilter;
import joshie.progression.api.special.DisplayMode;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public abstract class FilterBase implements IProgressionFilter {
    private static final Random rand = new Random();
    private int color;
    private String name;
    private String unformatted;
    private UUID uuid;

    public FilterBase(String name, int color) {
        this.name = "filter." + name;
        this.unformatted = name;
        this.color = color;
    }

    public static IProgressionFilter getRandomFilterFromFilters(List<IProgressionFilter> locality) {
        int size = locality.size();
        if (size == 0) return null;
        if (size == 1) return locality.get(0);
        else {
            return locality.get(rand.nextInt(size));
        }
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public String getUnlocalisedName() {
        return name;
    }

    @Override
    public String getLocalisedName() {
        return Progression.translate("filter." + getType().getName().toLowerCase() + "." + unformatted);
    }

    @Override
    public UUID getUniqueID() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }

        return uuid;
    }

    @Override
    public void updateDraw() {}

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public String getDescription() {
        return Progression.translate("filter." + getType().getName().toLowerCase() + "." + unformatted + ".description");
    }

    @Override
    public boolean matches(Object object) {
        return false;
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return 100;
    }
}
