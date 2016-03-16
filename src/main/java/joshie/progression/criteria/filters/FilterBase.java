package joshie.progression.criteria.filters;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import joshie.progression.Progression;
import joshie.progression.api.IField;
import joshie.progression.api.IFieldProvider;
import joshie.progression.api.IItemFilter;
import net.minecraft.item.ItemStack;

public class FilterBase implements IItemFilter {
    protected List<IField> list = new ArrayList();
    private int color;
    protected String name;

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
    public void readFromJSON(JsonObject data) {}

    @Override
    public void writeToJSON(JsonObject data) {}
    
    @Override
    public IFieldProvider getProvider() {
        return this;
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
