package joshie.progression.gui.fields;

import joshie.progression.Progression;
import joshie.progression.api.special.IHideable;

public class BooleanFieldHideable extends BooleanField implements IHideable {
    public BooleanFieldHideable(String name, Object object) {
        super(name, object);
    }
    
    @Override
    public String getField() {
        return isVisible() ? Progression.translate("hideable." + name) : "";
    }

    @Override
    public boolean isVisible() {
        boolean result = false;
        try {
            result = getBoolean();
        } catch (Exception e) {}
        
        return result == true;
    }
}
