package joshie.progression.gui.fields;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import joshie.progression.Progression;
import joshie.progression.api.criteria.IProgressionFilter;
import joshie.progression.api.criteria.IProgressionFilterSelector;
import joshie.progression.api.special.IInit;
import joshie.progression.api.special.ISetterCallback;
import joshie.progression.api.special.ISpecialFilters;
import joshie.progression.gui.core.DrawHelper;
import joshie.progression.gui.core.GuiCore;
import joshie.progression.gui.editors.GuiItemFilterEditor;
import joshie.progression.gui.filters.FeatureItemPreview;
import joshie.progression.gui.filters.FilterSelectorItem;
import joshie.progression.helpers.CollectionHelper;

public class ItemFilterField extends AbstractField {
    private IProgressionFilterSelector selector;
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
        } else selector = FilterSelectorItem.INSTANCE;
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
        helper.drawSplitText(renderX, renderY, Progression.translate("filter." + selector.getType().name().toLowerCase()), 4, yPos, 105, color, 0.75F);
    }

    public boolean isAccepted(IProgressionFilter filter) {
        if (filter.getType() != selector.getType()) return false;
        return true;
    }

    @Override
    public void click() {
        try {
            GuiItemFilterEditor.INSTANCE.setPrevious(GuiCore.INSTANCE.openGui).setFilterSet(this); //Adjust this filter object
            FeatureItemPreview.INSTANCE.select(selector); //Allow for selection of multiple items 
            GuiCore.INSTANCE.setEditor(GuiItemFilterEditor.INSTANCE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean attemptClick(int mouseX, int mouseY) {
        return false;
    }

    public void setFilters(List<IProgressionFilter> filters) {
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

    public List<IProgressionFilter> getFilters() {
        try {
            return (List<IProgressionFilter>) field.get(object);
        } catch (Exception e) {}

        //Return a blank list yo!
        return new ArrayList();
    }

    public void add(IProgressionFilter filter) {
        List<IProgressionFilter> filters = getFilters();
        filters.add(filter);
        if (object instanceof ISetterCallback) {
            ((ISetterCallback) object).setField(field.getName(), filters);
        }

        //Call this on init objects
        if (object instanceof IInit) {
            ((IInit) object).init();
        }
    }

    public void remove(IProgressionFilter filter) {
        List<IProgressionFilter> filters = getFilters();
        CollectionHelper.remove(filters, filter);
        if (object instanceof ISetterCallback) {
            ((ISetterCallback) object).setField(field.getName(), filters);
        }

        //Call this on init objects
        if (object instanceof IInit) {
            ((IInit) object).init();
        }
    }
}
