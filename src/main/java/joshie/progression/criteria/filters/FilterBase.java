package joshie.progression.criteria.filters;

import joshie.progression.Progression;
import joshie.progression.api.IItemFilter;
import net.minecraft.item.ItemStack;

public abstract class FilterBase implements IItemFilter {
    private int color;
    private String name;

    public FilterBase(String name, int color) {
        this.name = name;
        this.color = color;
    }

    @Override
    public String getUnlocalisedName() {
        return name;
    }

    @Override
    public String getLocalisedName() {
        return Progression.translate("filter.item." + getUnlocalisedName());
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
    public boolean matches(ItemStack stack) {
        return false;
    }
}
