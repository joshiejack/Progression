package joshie.progression.gui.fields;

import joshie.progression.api.IFieldRegistry;
import joshie.progression.api.criteria.IFieldProvider;
import joshie.progression.api.criteria.IProgressionField;

public class FieldRegistry implements IFieldRegistry {
    @Override
    public IProgressionField getItemPreview(IFieldProvider provider, String string, int x, int y, float scale) {
        return new ItemFilterFieldPreview(string, provider, x, y, scale);
    }
}
