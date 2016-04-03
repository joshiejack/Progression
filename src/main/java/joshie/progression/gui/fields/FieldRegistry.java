package joshie.progression.gui.fields;

import joshie.progression.api.IFieldRegistry;
import joshie.progression.api.criteria.IField;
import joshie.progression.api.criteria.IFieldProvider;

public class FieldRegistry implements IFieldRegistry {
    @Override
    public IField getItemPreview(IFieldProvider provider, String string, int x, int y, float scale) {
        return new ItemFilterFieldPreview(string, provider, x, y, scale);
    }

    @Override
    public IField getItem(IFieldProvider provider, String string, int x, int y, float scale) {
        return new ItemField(string, provider, x, y, scale);
    }
}
