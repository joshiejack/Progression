package joshie.progression.gui.fields;

import joshie.progression.api.criteria.IRuleProvider;
import joshie.progression.api.gui.IDrawHelper;
import joshie.progression.api.special.IInit;

import java.lang.reflect.Field;

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

    public BooleanField(String displayName, String fieldName, Object object, String truth, String lies) {
        this(displayName, fieldName, object);
        this.truth = truth;
        this.lies = lies;
    }

    public boolean getBoolean() {
        try {
            return field.getBoolean(object);
        } catch (Exception e) { return false; }
    }

    public void setBoolean(boolean bool) {
        try {
            field.set(object, bool);
            //Init the object after we've set it
            if (object instanceof IInit) {
                ((IInit) object).init(true);
            }
        } catch (Exception e) {}
    }

    public BooleanField(String name, Object object) {
        this(name, name, object);
    }

    @Override
    public String getFieldName() {
        return field.getName();
    }

    @Override
    public boolean click() {
        try {
            setBoolean(!getBoolean());

            return true;
        } catch (Exception e) { return false; }
    }

    @Override
    public String getField() {
        try {
            return "" + getBoolean();
        } catch (Exception e) {
            return "" + false;
        }
    }

    @Override
    public void draw(IRuleProvider provider, IDrawHelper helper, int renderX, int renderY, int color, int yPos, int mouseX, int mouseY) {
        try {
            boolean value = getBoolean();
            if (truth != null) helper.drawSplitText(renderX, renderY, name + ": " + (value ? truth : lies), 4, yPos, 105, color, 0.75F);
            else helper.drawSplitText(renderX, renderY, name + ": " + value, 4, yPos, 125, color, 0.75F);
        } catch (Exception e) {}
    }
}