package joshie.progression.gui.fields;

import java.lang.reflect.Field;

import joshie.progression.gui.editors.IItemSelectable;
import joshie.progression.gui.newversion.overlays.DrawFeatureHelper;
import joshie.progression.gui.newversion.overlays.FeatureItemSelector;
import joshie.progression.gui.newversion.overlays.FeatureItemSelector.Type;
import joshie.progression.gui.newversion.overlays.IItemSelectorFilter;
import net.minecraft.item.ItemStack;

public class ItemField extends AbstractField implements IItemSelectable {
    private Field field;
    protected Object object;
    private final int x;
    private final int y;
    private final float scale;
    protected final int mouseX1;
    protected final int mouseX2;
    protected final int mouseY1;
    protected final int mouseY2;
    protected final Type type;
    protected final IItemSelectorFilter filter;
    
    public ItemField(String fieldName, Object object, int x, int y, float scale, int mouseX1, int mouseX2, int mouseY1, int mouseY2, Type type, IItemSelectorFilter... filters) {
        super(fieldName);
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.mouseX1 = mouseX1;
        this.mouseX2 = mouseX2;
        this.mouseY1 = mouseY1;
        this.mouseY2 = mouseY2;
        this.type = type;
        if (filters == null || filters.length == 0) filter = null;
        else filter = filters[0];
        
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
            FeatureItemSelector.INSTANCE.select(filter, this, type);
            return true;
        } else return false;
    }
    
    public ItemStack getStack() {
        try {
            if (object instanceof IItemGetterCallback) {
                ItemStack ret = ((IItemGetterCallback)object).getItem(field.getName());
                if (ret != null) return ret;
            }
            
            return (ItemStack) field.get(object);
        } catch (Exception e) { return null; }
    }
    
    @Override
    public String getField() {
        try {
            return getStack().getDisplayName();
        } catch (Exception e) { return ""; } 
    }

    @Override
    public void draw(DrawFeatureHelper helper, int renderX, int renderY, int color, int yPos) {
        try {
            helper.drawStack(renderX, renderY, getStack(), x, y, scale);
        } catch (Exception e) {}
    }

    @Override
    public void setItemStack(ItemStack stack) {
        try {
            if (object instanceof IItemSetterCallback) {
                ((IItemSetterCallback)object).setItem(field.getName(), stack);
            } else field.set(object, stack);
        } catch (Exception e) {}
    }
    
    @Override
    public void setObject(Object object) {
        this.object = object;
    }
}