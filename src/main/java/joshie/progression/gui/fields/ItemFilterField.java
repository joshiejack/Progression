package joshie.progression.gui.fields;

import joshie.progression.Progression;
import joshie.progression.api.criteria.IFilter;
import joshie.progression.api.criteria.IFilterType;
import joshie.progression.api.special.IInit;
import joshie.progression.api.special.ISetterCallback;
import joshie.progression.api.special.ISpecialFilters;
import joshie.progression.gui.core.DrawHelper;
import joshie.progression.gui.core.GuiCore;
import joshie.progression.gui.editors.GuiFilterEditor;
import joshie.progression.gui.filters.FeatureItemPreview;
import joshie.progression.gui.filters.FilterTypeItem;
import joshie.progression.helpers.CollectionHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ItemFilterField extends AbstractField {
    private IFilterType selector;
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
        } else selector = FilterTypeItem.INSTANCE;
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
    public void draw(DrawHelper helper, int renderX, int renderY, int color, int yPos, int mouseX, int mouseY) {
        helper.drawSplitText(renderX, renderY, Progression.translate("filter." + selector.getName()), 4, yPos, 105, color, 0.75F);
    }

    public boolean isAccepted(IFilter filter) {
        if (filter.getType() != selector) return false;
        return true;
    }

    @Override
    public void click() {
        try {
            GuiFilterEditor.INSTANCE.setPrevious(GuiCore.INSTANCE.openGui).setFilterSet(this); //Adjust this filter object
            FeatureItemPreview.INSTANCE.select(selector); //Allow for selection of multiple items 
            GuiCore.INSTANCE.setEditor(GuiFilterEditor.INSTANCE);
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

            //Update
            FeatureItemPreview.INSTANCE.updateSearch();
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

        //Call this on init objects
        if (object instanceof IInit) {
            ((IInit) object).init();
        }

        //Update
        FeatureItemPreview.INSTANCE.updateSearch();
    }

    public void remove(IFilter filter) {
        List<IFilter> filters = getFilters();
        CollectionHelper.remove(filters, filter);
        if (object instanceof ISetterCallback) {
            ((ISetterCallback) object).setField(field.getName(), filters);
        }

        //Call this on init objects
        if (object instanceof IInit) {
            ((IInit) object).init();
        }

        //Update
        FeatureItemPreview.INSTANCE.updateSearch();
    }
}
