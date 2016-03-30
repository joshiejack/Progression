package joshie.progression.criteria.filters;

import joshie.progression.Progression;
import joshie.progression.api.criteria.IProgressionFilter;
import joshie.progression.api.special.DisplayMode;

import java.util.UUID;

public abstract class FilterBase implements IProgressionFilter {
    private int color;
    private String name;
    private String unformatted;
    private UUID uuid;

    public FilterBase(String name, int color) {
        this.name = "filter." + name;
        this.unformatted = name;
        this.color = color;
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
        return "";
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
