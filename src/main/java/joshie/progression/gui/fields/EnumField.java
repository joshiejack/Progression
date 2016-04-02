package joshie.progression.gui.fields;

import joshie.progression.api.special.IEnum;
import joshie.progression.api.special.IInit;
import joshie.progression.gui.core.DrawHelper;

import java.lang.reflect.Field;

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

        //Init the object after we've set it
        if (object instanceof IInit) {
            ((IInit) object).init();
        }
    }

    @Override
    public String getFieldName() {
        return field.getName();
    }

    @Override
    public void click() {
        try {
            setField(object.next(getFieldName()));
        } catch (Exception e) {}
    }

    @Override
    public String getField() {
        try {
            return getName().toString().toLowerCase();
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public void draw(DrawHelper helper, int renderX, int renderY, int color, int yPos, int mouseX, int mouseY) {
        try {
            String value = getName().toString().toLowerCase();
            helper.drawSplitText(renderX, renderY, name + ": " + value, 4, yPos, 150, color, 0.75F);
        } catch (Exception e) {}
    }

    @Override
    public void setObject(Object object) {
        this.object = (IEnum) object;
    }
}