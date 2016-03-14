package joshie.progression.gui.fields;

import java.lang.reflect.Field;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.gui.newversion.overlays.DrawFeatureHelper;

public class BooleanField extends AbstractField {
    public Field field;
    public String truth;
    public String lies;

    public BooleanField(String displayName, String fieldName, Object object) {
        super(displayName);
        this.object = object;

        try {
            field = object.getClass().getField(fieldName);
        } catch (Exception e) {}
    }

    public BooleanField(String displayName, Object object, String truth, String lies) {
        this(displayName, displayName, object, truth, lies);
    }

    public BooleanField(String displayName, String fieldName, Object object, String truth, String lies) {
        this(displayName, fieldName, object);
        this.truth = truth;
        this.lies = lies;
    }

    public boolean getBoolean() throws IllegalArgumentException, IllegalAccessException {
        return field.getBoolean(object);
    }

    public void setBoolean(boolean bool) throws IllegalArgumentException, IllegalAccessException {
        field.set(object, bool);
    }

    public BooleanField(String name, Object object) {
        this(name, name, object);
    }
    
    @Override
    public String getFieldName() {
        return field.getName();
    }

    @Override
    public void click() {
        try {
            setBoolean(!getBoolean());
        } catch (Exception e) {}
    }
    
    @Override
    public String getField() {
        try {
            return "" + getBoolean();
        } catch (Exception e) { return "" + false; }
    }

    @Override
    public void draw(DrawFeatureHelper helper, int renderX, int renderY, int color, int yPos) {
        try {
            boolean value = getBoolean();
            if (truth != null) helper.drawSplitText(renderX, renderY, name + ": " + (value ? truth : lies), 4, yPos, 105, color, 0.75F);
            else helper.drawSplitText(renderX, renderY, name + ": " + value, 4, yPos, 105, color, 0.75F);
        } catch (Exception e) {}
    }
}