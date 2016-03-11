package joshie.progression.gui.fields;

import java.lang.reflect.Field;

import joshie.progression.gui.newversion.overlays.DrawFeatureHelper;

public class EnumField extends AbstractField {
    public Field field;
    public IEnum object;

    public EnumField(String displayName, String fieldName, IEnum object) {
        super(displayName);
        this.object = object;

        try {
            field = object.getClass().getField(fieldName);
        } catch (Exception e) {}
    }

    public EnumField(String name, IEnum object) {
        this(name, name, object);
    }

    public Enum getName() throws IllegalArgumentException, IllegalAccessException {
        return (Enum) field.get(object);
    }

    public void setField(Object next) throws IllegalArgumentException, IllegalAccessException {
        field.set(object, next);
    }

    @Override
    public void click() {
        try {
            setField(object.next());
        } catch (Exception e) {}
    }
    
    @Override
    public String getField() {
        try {
            return getName().toString().toLowerCase();
        } catch (Exception e) { return ""; }
    }

    @Override
    public void draw(DrawFeatureHelper helper, int renderX, int renderY, int color, int yPos) {
        try {
            String value = getName().toString().toLowerCase();
            helper.drawSplitText(renderX, renderY, name + ": " + value, 4, yPos, 105, color);
        } catch (Exception e) {}
    }

    @Override
    public void setObject(Object object) {
        this.object = (IEnum) object;
    }
}