package joshie.progression.gui.fields;

import joshie.progression.gui.fields.FieldHelper.ItemAmountFieldHelper;
import joshie.progression.gui.newversion.overlays.FeatureItemSelector.Type;

public class ItemAmountField extends TextField {
    public ItemAmountField(String displayName, String fieldName, ItemField object, Type type) {
        super(displayName, fieldName, object, type);
        this.data = new ItemAmountFieldHelper(fieldName, object, type);
    }
}