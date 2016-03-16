package joshie.progression.gui.fields;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import joshie.enchiridion.helpers.MCClientHelper;
import joshie.progression.Progression;
import joshie.progression.api.IBlocksOnly;
import joshie.progression.api.IFilter;
import joshie.progression.api.ISetterCallback;
import joshie.progression.gui.newversion.GuiCriteriaEditor;
import joshie.progression.gui.newversion.GuiItemFilterEditor;
import joshie.progression.gui.newversion.overlays.DrawFeatureHelper;
import joshie.progression.gui.newversion.overlays.FeatureItemPreview;
import joshie.progression.gui.newversion.overlays.IItemSelectorFilter;
import joshie.progression.gui.selector.filters.BlockFilter;
import joshie.progression.helpers.CollectionHelper;

public class ItemFilterField extends AbstractField {
    private String[] accepted;
    private Field field;

    public ItemFilterField(String fieldName, Object object, String... accepted) {
        super(fieldName);
        this.object = object;
        this.accepted = accepted;

        try {
            field = object.getClass().getField(fieldName);
        } catch (Exception e) {
            try {
                field = object.getClass().getSuperclass().getField(fieldName);
            } catch (Exception e1) {}
        }
    }

    @Override
    public String getFieldName() {
        return field.getName();
    }

    @Override
    public String getField() {
        return "";
    }

    @Override
    public void draw(DrawFeatureHelper helper, int renderX, int renderY, int color, int yPos) {
        helper.drawSplitText(renderX, renderY, "Item Editor", 4, yPos, 105, color, 0.75F);
    }

    public boolean isAccepted(IFilter filter) {
        if (object instanceof IBlocksOnly) {
            if (!filter.getUnlocalisedName().startsWith("block")) return false;
        }
        
        if (accepted == null || accepted.length == 0) return true;
        else {           
            for (String string : accepted) {
                if (string.equalsIgnoreCase(filter.getUnlocalisedName())) return true;
            }

            return false;
        }
    }

    @Override
    public void click() {
        try {
            GuiItemFilterEditor.INSTANCE.setFilterSet(this); //Adjust this filter object
            FeatureItemPreview.INSTANCE.select(false); //Allow for selection of multiple items 
            GuiCriteriaEditor.INSTANCE.switching = true; //Don't save if we're switching guis
            MCClientHelper.getPlayer().openGui(Progression.instance, 3, MCClientHelper.getWorld(), 0, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean attemptClick(int mouseX, int mouseY) {
        return false;
    }

    public void setFilters(List<IFilter> filters) {
        try {
            if (object instanceof ISetterCallback) {
                ((ISetterCallback) object).setField(field.getName(), filters);
            } else field.set(object, filters);
        } catch (Exception e) {}
    }

    public List<IFilter> getFilters() {
        try {
            return (List<IFilter>) field.get(object);
        } catch (Exception e) {}

        //Return a blank list yo!
        return new ArrayList();
    }

    public void add(IFilter filter) {
        List<IFilter> filters = getFilters();
        filters.add(filter);
        if (object instanceof ISetterCallback) {
            ((ISetterCallback) object).setField(field.getName(), filters);
        }
    }

    public void remove(IFilter filter) {
        List<IFilter> filters = getFilters();
        CollectionHelper.remove(filters, filter);
        if (object instanceof ISetterCallback) {
            ((ISetterCallback) object).setField(field.getName(), filters);
        }
    }
}
