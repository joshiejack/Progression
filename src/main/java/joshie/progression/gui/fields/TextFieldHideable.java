package joshie.progression.gui.fields;

import joshie.progression.api.special.IHideable;
import joshie.progression.api.gui.Position;

public class TextFieldHideable extends TextField implements IHideable {
    private BooleanFieldHideable field;
    
    public TextFieldHideable(String name, Object object, Position type) {
        super(name, object, type);
        this.field = field;
    }

    public TextFieldHideable setBooleanField(BooleanFieldHideable bool) {
        this.field = bool;
        return this;
    }
    
    @Override
    public String getField() {
        return isVisible() ? super.getField() : field.getField();
    }

    @Override
    public void click(int button) {
        if (button != 0) {
            field.setBoolean(!field.getBoolean()); //Set the stuff
        } else if (isVisible()) super.click();
        else if (button != 0 && !isVisible()) {
            field.click();
        }
    }

    @Override
    public boolean isVisible() {
        return field.isVisible() == false;
    }
}
