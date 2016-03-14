package joshie.progression.gui.fields;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import joshie.enchiridion.helpers.MCClientHelper;
import joshie.progression.Progression;
import joshie.progression.api.IItemFilter;
import joshie.progression.gui.newversion.GuiCriteriaEditor;
import joshie.progression.gui.newversion.GuiItemFilterEditor;
import joshie.progression.gui.newversion.overlays.DrawFeatureHelper;
import joshie.progression.gui.newversion.overlays.FeatureItemPreview;
import joshie.progression.helpers.CollectionHelper;

public class ItemFilterField extends AbstractField {
    private Field field;

    public ItemFilterField(String fieldName, Object object) {
        super(fieldName);
        this.object = object;

        try {
            field = object.getClass().getField(fieldName);
        } catch (Exception e) {}
    }

    @Override
    public String getField() {
        return "";
    }

    @Override
    public void draw(DrawFeatureHelper helper, int renderX, int renderY, int color, int yPos) {
        helper.drawSplitText(renderX, renderY, "Item Editor", 4, yPos, 105, color, 0.75F);
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

    public void setFilters(List<IItemFilter> filters) {
        try {
            if (object instanceof IItemFilterSetterCallback) {
                ((IItemFilterSetterCallback)object).setFilter(field.getName(), filters);
            } else field.set(object, filters);
        } catch (Exception e) {}
    }

    public List<IItemFilter> getFilters() {
        try {
            return (List<IItemFilter>) field.get(object);
        } catch (Exception e) {}

        //Return a blank list yo!
        return new ArrayList();
    }

    public void add(IItemFilter filter) {
        List<IItemFilter> filters = getFilters();
        filters.add(filter);
        if (object instanceof IItemFilterSetterCallback) {
            ((IItemFilterSetterCallback)object).setFilter(field.getName(), filters);
        }
    }

    public void remove(IItemFilter filter) {
        List<IItemFilter> filters = getFilters();
        CollectionHelper.remove(filters, filter);
        if (object instanceof IItemFilterSetterCallback) {
            ((IItemFilterSetterCallback)object).setFilter(field.getName(), filters);
        }
    }
}
