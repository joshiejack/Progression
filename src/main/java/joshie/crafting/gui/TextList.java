package joshie.crafting.gui;

import java.lang.reflect.Field;

import joshie.crafting.api.DrawHelper;
import joshie.crafting.gui.SelectItemOverlay.Type;
import joshie.crafting.gui.TextFieldHelper.FloatFieldHelper;
import joshie.crafting.gui.TextFieldHelper.IntegerFieldHelper;
import joshie.crafting.gui.TextFieldHelper.ItemAmountFieldHelper;
import net.minecraft.item.ItemStack;

public abstract class TextList {
    public String name;

    public TextList(String name) {
        this.name = name;
    }

    public abstract void click();

    public abstract void draw(int color, int yPos);

    public boolean attemptClick(int mouseX, int mouseY) {
        return false;
    }

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
    
    public static class ItemAmountField extends TextField {
        public ItemAmountField(String displayName, String fieldName, ItemField object) {
            super(displayName, fieldName, object);
            this.data = new ItemAmountFieldHelper(fieldName, object);
        }
    }
    
    public static class ItemField extends TextList implements IItemSelectable {
        private Field field;
        private Object object;
        private final int x;
        private final int y;
        private final float scale;
        private final int mouseX1;
        private final int mouseX2;
        private final int mouseY1;
        private final int mouseY2;
        private final Type type;
        
        public ItemField(String fieldName, Object object, int x, int y, float scale, int mouseX1, int mouseX2, int mouseY1, int mouseY2, Type type) {
            super(fieldName);
            this.x = x;
            this.y = y;
            this.scale = scale;
            this.mouseX1 = mouseX1;
            this.mouseX2 = mouseX2;
            this.mouseY1 = mouseY1;
            this.mouseY2 = mouseY2;
            this.type = type;
            
            try {
                this.field = object.getClass().getField(fieldName);
                this.object = object;
            } catch (Exception e) {}
        }

        @Override
        public void click() {}
        
        @Override
        public boolean attemptClick(int mouseX, int mouseY) {
            boolean clicked = mouseX >= mouseX1 && mouseX <= mouseX2 && mouseY >= mouseY1 && mouseY <= mouseY2;
            if (clicked) {
                SelectItemOverlay.INSTANCE.select(this, type);
                return true;
            } else return false;
        }
        
        public ItemStack getStack() {
            try {
                return (ItemStack) field.get(object);
            } catch (Exception e) { return null; }
        }

        @Override
        public void draw(int color, int yPos) {
            try {
                DrawHelper.drawStack(getStack(), x, y, scale);
            } catch (Exception e) {}
        }

        @Override
        public void setItemStack(ItemStack stack) {
            try {
                field.set(object, stack);
            } catch (Exception e) {}
        }
    }
}
