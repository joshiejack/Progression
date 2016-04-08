package joshie.progression.gui.fields;

import joshie.progression.Progression;
import joshie.progression.api.criteria.IFilter;
import joshie.progression.api.criteria.IFilterProvider;
import joshie.progression.api.criteria.IFilterType;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.IHasFilters;
import joshie.progression.api.special.IInit;
import joshie.progression.api.special.ISetterCallback;
import joshie.progression.gui.core.DrawHelper;
import joshie.progression.gui.core.GuiCore;
import joshie.progression.gui.editors.GuiFilterEditor;
import joshie.progression.gui.filters.FeatureItemPreview;
import joshie.progression.gui.filters.FilterTypeItem;
import joshie.progression.helpers.CollectionHelper;
import joshie.progression.helpers.MCClientHelper;
import net.minecraft.client.gui.GuiScreen;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemFilterField extends AbstractField {
    private static final Random rand = new Random();
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


        if (object instanceof IHasFilters) {
            selector = ((IHasFilters) object).getFilterForField(getFieldName());
        } else if (object instanceof IFilter) {
            selector = ((IFilter)object).getType();
        } else selector = FilterTypeItem.INSTANCE;
    }

    @Override
    public String getFieldName() {
        return field.getName();
    }

    private transient int ticker = 0;
    private transient String name;

    public IFilterProvider getRandomFilter() {
        List<IFilterProvider> filters = getFilters();
        int size = filters.size();
        if (size == 0) return null;
        if (size == 1) return filters.get(0);
        else {
            return filters.get(rand.nextInt(size));
        }
    }

    @Override
    public String getField() {
        if (ticker %200 == 0) {
            IFilterProvider filter = getRandomFilter();
            if (filter == null) name = null;
            else name = filter.getDescription();
        }

        if (!GuiScreen.isShiftKeyDown()) ticker++;
        return name == null ? "No Valid Filter Set" : name;
    }

    @Override
    public void draw(DrawHelper helper, int renderX, int renderY, int color, int yPos, int mouseX, int mouseY) {
        if (MCClientHelper.getMode() == DisplayMode.EDIT) {
            helper.drawSplitText(renderX, renderY, Progression.translate("filter." + selector.getName()), 4, yPos, 105, color, 0.75F);
        }
    }

    public boolean isAccepted(IFilterProvider filter) {
        if (filter.getProvided().getType() != selector) return false;
        return true;
    }

    @Override
    public boolean click() {
        try {
            GuiFilterEditor.INSTANCE.setPrevious(GuiCore.INSTANCE.openGui).setFilterSet(this); //Adjust this filter object
            FeatureItemPreview.INSTANCE.select(selector); //Allow for selection of multiple items 
            GuiCore.INSTANCE.setEditor(GuiFilterEditor.INSTANCE);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean attemptClick(int mouseX, int mouseY) {
        return false;
    }

    public void setFilters(List<IFilterProvider> filters) {
        try {
            if (object instanceof ISetterCallback) {
                ((ISetterCallback) object).setField(field.getName(), filters);
            } else field.set(object, filters);

            //Init the object after we've set it
            if (object instanceof IInit) {
                ((IInit) object).init(true);
            }

            //Update
            FeatureItemPreview.INSTANCE.updateSearch();
        } catch (Exception e) {}
    }

    public List<IFilterProvider> getFilters() {
        try {
            return (List<IFilterProvider>) field.get(object);
        } catch (Exception e) {}

        //Return a blank list yo!
        return new ArrayList();
    }

    public void add(IFilterProvider filter) {
        List<IFilterProvider> filters = getFilters();
        filters.add(filter);
        if (object instanceof ISetterCallback) {
            ((ISetterCallback) object).setField(field.getName(), filters);
        }

        //Call this on init objects
        if (object instanceof IInit) {
            ((IInit) object).init(true);
        }

        //Update
        FeatureItemPreview.INSTANCE.updateSearch();
    }

    public void remove(IFilterProvider filter) {
        List<IFilterProvider> filters = getFilters();
        CollectionHelper.remove(filters, filter);
        if (object instanceof ISetterCallback) {
            ((ISetterCallback) object).setField(field.getName(), filters);
        }

        //Call this on init objects
        if (object instanceof IInit) {
            ((IInit) object).init(true);
        }

        //Update
        FeatureItemPreview.INSTANCE.updateSearch();
    }
}
