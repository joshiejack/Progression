package joshie.progression.gui.fields;

import joshie.progression.api.IHideable;
import joshie.progression.gui.newversion.overlays.FeatureItemSelector.Type;

public class TextFieldHideable extends TextField implements IHideable {
    private BooleanFieldHideable field;
    
    public TextFieldHideable(BooleanFieldHideable field, String name, Object object, Type type) {
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
