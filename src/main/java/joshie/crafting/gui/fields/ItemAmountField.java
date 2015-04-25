package joshie.crafting.gui.fields;

import joshie.crafting.gui.TextFieldHelper.ItemAmountFieldHelper;

public class ItemAmountField extends TextField {
    public ItemAmountField(String displayName, String fieldName, ItemField object) {
        super(displayName, fieldName, object);
        this.data = new ItemAmountFieldHelper(fieldName, object);
    }
}