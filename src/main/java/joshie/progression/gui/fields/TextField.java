package joshie.progression.gui.fields;

import joshie.progression.api.criteria.IRuleProvider;
import joshie.progression.api.gui.IDrawHelper;
import joshie.progression.api.gui.Position;
import joshie.progression.api.special.IGetterCallback;
import joshie.progression.gui.fields.FieldHelper.DoubleFieldHelper;
import joshie.progression.gui.fields.FieldHelper.FloatFieldHelper;
import joshie.progression.gui.fields.FieldHelper.IntegerFieldHelper;

import static joshie.progression.api.special.DisplayMode.EDIT;

public class TextField extends AbstractField {
    protected FieldHelper data;
    private Position type;

    public TextField(String displayName, String fieldName, Object object, Position type) {
        super(displayName);
        this.data = getField(name, object, type);
        this.object = object;
        this.type = type;
    }

    public TextField(String name, Object object, Position type) {
        this(name, name, object, type);
    }

    @Override
    public String getFieldName() {
        return data.getFieldName();
    }

    @Override
    public boolean click() {
        data.select();
        return true;
    }

    @Override
    public String getField() {
        return data.getText();
    }

    @Override
    public void draw(IRuleProvider provider, IDrawHelper helper, int renderX, int renderY, int color, int yPos, int mouseX, int mouseY) {
        String datatext = data.getText();
        if (object instanceof IGetterCallback) {
            datatext = ((IGetterCallback) object).getField(data.getFieldName());
        }

        String totalString = getFieldName() + ": " + datatext;
        int totalLength = totalString.length();
        int number = totalLength > 48 ? totalLength - 48 : 48 - totalLength;
        int length = Math.max(0, number);
        String suffix = datatext.length() > length ? "..." : "";
        datatext = datatext.substring(0, Math.min(datatext.length(), length)) + suffix;
        helper.drawSplitText(renderX, renderY, name + ": " + datatext, 4, yPos, provider.getWidth(EDIT) + 48, color, 0.75F);
    }

    public static FieldHelper getField(String name, Object object, Position type) {
        try {
            Class clazz = object.getClass().getField(name).getType();
            String className = clazz.getSimpleName();
            if (className.equalsIgnoreCase("double")) return new DoubleFieldHelper(name, object, type);
            if (className.equalsIgnoreCase("float")) return new FloatFieldHelper(name, object, type);
            if (className.equalsIgnoreCase("int")) return new IntegerFieldHelper(name, object, type);
            if (className.equalsIgnoreCase("string")) return new FieldHelper(name, object, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void setObject(Object object) {
        if (this.data == null) {
            this.data = getField(name, object, type);
        }
    }
}