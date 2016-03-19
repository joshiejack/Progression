package joshie.progression.gui.fields;

import joshie.progression.api.fields.IField;
import joshie.progression.api.fields.IFieldProvider;
import joshie.progression.api.fields.IFieldRegistry;

public class FieldRegistry implements IFieldRegistry {
    @Override
    public IField getItemPreview(IFieldProvider provider, String string, int x, int y, float scale) {
        return new ItemFilterFieldPreview(string, provider, x, y, scale);
    }
}
