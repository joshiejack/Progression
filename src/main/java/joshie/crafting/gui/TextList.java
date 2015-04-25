package joshie.crafting.gui;

import java.lang.reflect.Field;

import joshie.crafting.api.DrawHelper;
import joshie.crafting.gui.TextFieldHelper.FloatFieldHelper;
import joshie.crafting.gui.TextFieldHelper.IntegerFieldHelper;

public abstract class TextList {
    public String name;

    public TextList(String name) {
        this.name = name;
    }

    public abstract void click();

    public abstract void draw(int color, int yPos);

    public static class BooleanField extends TextList {
        public Field field;
        public Object object;
        public String truth;
        public String lies;

        public BooleanField(String displayName, String fieldName, Object object) {
            super(displayName);
            this.object = object;

            try {
                field = object.getClass().getField(fieldName);
            } catch (Exception e) {}
        }
        
        public BooleanField(String displayName, Object object, String truth, String lies) {
            this(displayName, displayName, object, truth, lies);
        }

        public BooleanField(String displayName, String fieldName, Object object, String truth, String lies) {
            this(displayName, fieldName, object);
            this.truth = truth;
            this.lies = lies;
        }

        public boolean getBoolean() throws IllegalArgumentException, IllegalAccessException {
            return field.getBoolean(object);
        }

        public void setBoolean(boolean bool) throws IllegalArgumentException, IllegalAccessException {
            field.set(object, bool);
        }

        public BooleanField(String name, Object object) {
            this(name, name, object);
        }

        @Override
        public void click() {
            try {
                setBoolean(!getBoolean());
            } catch (Exception e) {}
        }

        @Override
        public void draw(int color, int yPos) {
            try {
                boolean value = getBoolean();
                if (truth != null) {
                    DrawHelper.drawSplitText(name + ": " + (value ? truth: lies), 4, yPos, 105, color);
                } else {
                    DrawHelper.drawSplitText(name + ": " + value, 4, yPos, 105, color);
                }
            } catch (Exception e) {}
        }
    }

    public static class TextField extends TextList {
        protected TextFieldHelper data;

        public TextField(String displayName, String fieldName, Object object) {
            super(displayName);
            this.data = getField(name, object);
        }

        public TextField(String name, Object object) {
            this(name, name, object);
        }

        @Override
        public void click() {
            data.select();
        }

        @Override
        public void draw(int color, int yPos) {
            DrawHelper.drawSplitText(name + ": " + data, 4, yPos, 105, color);
        }

        public static TextFieldHelper getField(String name, Object object) {
            try {
                Class clazz = object.getClass().getField(name).getType();
                String className = clazz.getSimpleName();
                if (className.equalsIgnoreCase("float")) return new FloatFieldHelper(name, object);
                if (className.equalsIgnoreCase("int")) return new IntegerFieldHelper(name, object);
                if (className.equalsIgnoreCase("string")) return new TextFieldHelper(name, object);
            } catch (Exception e) {}

            return null;
        }
    }
}
