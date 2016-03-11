package joshie.progression.gui.fields;

import joshie.progression.api.IHideable;

public class TextFieldHideable extends TextField implements IHideable {
    private BooleanFieldHideable field;
    
    public TextFieldHideable(BooleanFieldHideable field, String name, Object object) {
        super(name, object);
        this.field = field;
    }
    
    @Override
    public String getField() {
        return isVisible() ? super.getField() : "";
    }

    @Override
    public boolean isVisible() {
        return field.isVisible() == false;
    }
}
