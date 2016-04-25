package joshie.progression.gui.fields;

import joshie.progression.api.IFieldRegistry;
import joshie.progression.api.criteria.IField;
import joshie.progression.api.criteria.IRewardProvider;
import joshie.progression.api.gui.Position;

public class FieldRegistry implements IFieldRegistry {
    @Override
    public IField getItemPreview(Object provider, String string, int x, int y, float scale) {
        return new ItemFilterFieldPreview(string, provider, x, y, scale);
    }

    @Override
    public IField getItem(Object provider, String string, int x, int y, float scale) {
        return new ItemField(string, provider, x, y, scale);
    }

    @Override
    public IField getFilter(Object provider, String name) {
        return new ItemFilterField(name, provider);
    }

    @Override
    public IField getBoolean(Object provider, String name) {
        return new BooleanField(name, provider);
    }

    @Override
    public IField getToggleBoolean(Object provider, String booleanName, String stringName) {
        Position position = provider instanceof IRewardProvider ? Position.TOP : Position.BOTTOM;
        return new TextFieldHideable(stringName, provider, position).setBooleanField(new BooleanFieldHideable(booleanName, provider));
    }

    @Override
    public IField getText(Object provider, String name) {
        Position position = provider instanceof IRewardProvider ? Position.TOP : Position.BOTTOM;
        return new TextField(name, provider, position);
    }
}
