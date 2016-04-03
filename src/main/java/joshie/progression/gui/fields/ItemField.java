package joshie.progression.gui.fields;

import joshie.progression.api.criteria.IFilter;
import joshie.progression.api.criteria.IFilterType;
import joshie.progression.api.special.*;
import joshie.progression.gui.core.DrawHelper;
import joshie.progression.gui.core.FeatureTooltip;
import joshie.progression.gui.editors.FeatureItemSelector;
import joshie.progression.gui.editors.FeatureItemSelector.Position;
import joshie.progression.gui.editors.IItemSelectable;
import joshie.progression.gui.filters.FeatureItemPreview;
import joshie.progression.gui.filters.FilterTypeItem;
import joshie.progression.helpers.MCClientHelper;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ItemField extends AbstractField implements IItemSelectable {
    private final IFilterType filter;
    private Field field;
    private Object object;
    private final int x;
    private final int y;
    private final float scale;
    private final int mouseX1;
    private final int mouseX2;
    private final int mouseY1;
    private final int mouseY2;

    public ItemField(String fieldName, Object object, int x, int y, float scale) {
        super(fieldName);
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.mouseX1 = x;
        this.mouseX2 = (int) (x + 14 * scale);
        this.mouseY1 = y - 2;
        this.mouseY2 = (int) (y + 15 * scale);

        if (object instanceof IFilter) {
            this.filter = ((IFilter)object).getType();
        } else if (object instanceof ISpecialFilters) {
            this.filter = ((ISpecialFilters) object).getFilterForField(fieldName);
        } else this.filter = FilterTypeItem.INSTANCE;

        try {
            this.field = object.getClass().getField(fieldName);
            this.object = object;
        } catch (Exception e) {}
    }

    @Override
    public void click() {}

    @Override
    public String getFieldName() {
        return field.getName();
    }

    @Override
    public boolean attemptClick(int mouseX, int mouseY) {
        boolean clicked = mouseX >= mouseX1 && mouseX <= mouseX2 && mouseY >= mouseY1 && mouseY <= mouseY2;
        if (clicked) {
            FeatureItemSelector.INSTANCE.select(filter, this, Position.BOTTOM);
            return true;
        } else return false;
    }

    public ItemStack getStack() {
        try {
            if (object instanceof IItemGetterCallback) {
                ItemStack ret = ((IItemGetterCallback) object).getItem(field.getName());
                if (ret != null) return ret;
            }

            return (ItemStack) field.get(object);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getField() {
        try {
            return getStack().getDisplayName();
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public void draw(DrawHelper helper, int renderX, int renderY, int color, int yPos, int mouseX, int mouseY) {
        try {
            boolean hovered = mouseX >= mouseX1 && mouseX <= mouseX2 && mouseY >= mouseY1 && mouseY <= mouseY2;
            if (hovered) {
                List<String> tooltip = new ArrayList();
                tooltip.addAll(getStack().getTooltip(MCClientHelper.getPlayer(), false));
                if (object instanceof IAdditionalTooltip) {
                    ((IAdditionalTooltip)object).addHoverTooltip(tooltip);
                }

                FeatureTooltip.INSTANCE.addTooltip(tooltip);
            }

            helper.drawStack(renderX, renderY, getStack(), x, y, scale);
        } catch (Exception e) {}
    }

    @Override
    public void setObject(Object object) {
        if (object instanceof ItemStack) {
            try {
                ItemStack stack = ((ItemStack) object).copy();
                if (this.object instanceof ISetterCallback) {
                    ((ISetterCallback) this.object).setField(field.getName(), stack);
                } else field.set(this.object, stack);

                //Init the object after we've set it
                if (this.object instanceof IInit) {
                    ((IInit) this.object).init();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Update
            FeatureItemPreview.INSTANCE.updateSearch();
        }
    }
}