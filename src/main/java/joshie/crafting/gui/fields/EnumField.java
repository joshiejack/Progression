package joshie.crafting.gui.fields;

import java.lang.reflect.Field;

import joshie.crafting.api.DrawHelper;

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
    public void draw(int color, int yPos) {
        try {
            String value = getName().toString().toLowerCase();
            DrawHelper.drawSplitText(name + ": " + value, 4, yPos, 105, color);
        } catch (Exception e) {}
    }

    @Override
    public void setObject(Object object) {
        this.object = (IEnum) object;
    }
}