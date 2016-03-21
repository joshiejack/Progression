package joshie.progression.gui.fields;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import joshie.progression.Progression;
import joshie.progression.api.IFilter;
import joshie.progression.api.ISetterCallback;
import joshie.progression.api.ISpecialFilters;
import joshie.progression.api.fields.IInit;
import joshie.progression.api.filters.IFilterSelectorFilter;
import joshie.progression.gui.newversion.GuiCriteriaEditor;
import joshie.progression.gui.newversion.GuiItemFilterEditor;
import joshie.progression.gui.newversion.overlays.DrawFeatureHelper;
import joshie.progression.gui.newversion.overlays.FeatureItemPreview;
import joshie.progression.gui.selector.filters.ItemFilter;
import joshie.progression.helpers.CollectionHelper;
import joshie.progression.helpers.MCClientHelper;

public class ItemFilterField extends AbstractField {
    private IFilterSelectorFilter selector;
    private Field field;

    public ItemFilterField(String fieldName, Object object) {
        super(fieldName);
        this.object = object;

        try {
            field = object.getClass().getField(fieldName);
        } catch (Exception e) {
            try {
                field = object.getClass().getSuperclass().getField(fieldName);
            } catch (Exception e1) {}
        }

        if (object instanceof ISpecialFilters) {
            selector = ((ISpecialFilters) object).getFilterForField(getFieldName());
        } else selector = ItemFilter.INSTANCE;
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
    public void draw(DrawFeatureHelper helper, int renderX, int renderY, int color, int yPos, int mouseX, int mouseY) {
        helper.drawSplitText(renderX, renderY, "Item Editor", 4, yPos, 105, color, 0.75F);
    }

    public boolean isAccepted(IFilter filter) {
        if (object instanceof ISpecialFilters) {
            if (selector != null) {
                if (filter.getType() != selector.getType()) return false;
            }
        }

        return true;
    }

    @Override
    public void click() {
        try {
            GuiItemFilterEditor.INSTANCE.setFilterSet(this); //Adjust this filter object
            FeatureItemPreview.INSTANCE.select(selector); //Allow for selection of multiple items 
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

            //Init the object after we've set it
            if (object instanceof IInit) {
                ((IInit) object).init();
            }
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
