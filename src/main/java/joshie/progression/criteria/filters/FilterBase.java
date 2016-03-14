package joshie.progression.criteria.filters;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import joshie.progression.Progression;
import joshie.progression.api.IField;
import joshie.progression.api.IItemFilter;
import joshie.progression.gui.newversion.GuiItemFilterEditor;
import joshie.progression.helpers.CollectionHelper;
import joshie.progression.json.Theme;
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
    public String getName() {
        return name;
    }
    
    @Override
    public String getLocalisedName() {
        return Progression.translate("filter.item." + getName());
    }

    @Override
    public void readFromJSON(JsonObject data) {}

    @Override
    public void writeToJSON(JsonObject data) {}
    
    @Override
    public List<IField> getFields() {
        return list;
    }

    @Override
    public void remove(List list) {
        GuiItemFilterEditor.INSTANCE.field.remove(this);
        GuiItemFilterEditor.INSTANCE.initGui();
    }

    @Override
    public void update() {}

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public int getGradient1() {
        return Theme.INSTANCE.triggerGradient1;
    }
    
    @Override
    public int getGradient2() {
        return Theme.INSTANCE.triggerGradient2;
    }
    
    @Override
    public int getFontColor() {
        return Theme.INSTANCE.triggerFontColor;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public void drawDisplay(int mouseX, int mouseY) {}

    @Override
    public boolean matches(ItemStack stack) {
        return false;
    }
}
