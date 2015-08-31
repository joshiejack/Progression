package joshie.progression.gui.fields;

import joshie.progression.gui.fields.FieldHelper.ItemAmountFieldHelper;

public class ItemAmountField extends TextField {
    public ItemAmountField(String displayName, String fieldName, ItemField object) {
        super(displayName, fieldName, object);
        this.data = new ItemAmountFieldHelper(fieldName, object);
    }
}