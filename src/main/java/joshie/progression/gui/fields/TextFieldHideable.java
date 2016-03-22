package joshie.progression.gui.fields;

import joshie.progression.api.special.IHideable;
import joshie.progression.gui.editors.FeatureItemSelector.Position;

public class TextFieldHideable extends TextField implements IHideable {
    private BooleanFieldHideable field;
    
    public TextFieldHideable(BooleanFieldHideable field, String name, Object object, Position type) {
        super(name, object, type);
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
