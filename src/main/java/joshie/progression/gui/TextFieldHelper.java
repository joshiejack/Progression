package joshie.progression.gui;

import java.lang.reflect.Field;

import joshie.progression.gui.SelectTextEdit.ITextEditable;
import joshie.progression.gui.fields.ICallback;
import joshie.progression.gui.fields.ItemField;
import net.minecraft.item.ItemStack;

public class TextFieldHelper implements ITextEditable {
    public Object o;
    public Field f;

    public TextFieldHelper() {}

    public TextFieldHelper(String f, Object o) {
        this.o = o;
        
        try {
            this.f = o.getClass().getField(f);
        } catch (Exception e) { }
    }

    public void select() {
        SelectTextEdit.INSTANCE.select(this);
    }

    public String getText() {
        return SelectTextEdit.INSTANCE.getText(this);
    }
    
    public float getFloat() {
        try {
            return (Float) f.get(o);
        } catch (Exception e) {}

        return 0F;
    }

    public double getDouble() {
        try {
            return (Double) f.get(o);
        } catch (Exception e) {}

        return 0D;
    }

    public int getInteger() {
        try {
            return (Integer) f.get(o);
        } catch (Exception e) {}

        return 0;
    }

    public String getString() {
        try {
            return (String) f.get(o);
        } catch (Exception e) {}

        return "";
    }

    public void set(Object o2) {
        try {
            f.set(o, o2);
        } catch (Exception e) { }
    }

    @Override
    public String getTextField() {
        return getString();
    }

    @Override
    public void setTextField(String str) {
        if (o instanceof ICallback) {
            ((ICallback)o).setField(str);
        } else set(str);
    }

    public static class IntegerFieldHelper extends TextFieldHelper {
        protected String textField = null;

        public IntegerFieldHelper() {}

        public IntegerFieldHelper(String f, Object o) {
            super(f, o);
        }

        @Override
        public String getTextField() {
            if (textField == null) {
                textField = "" + getInteger();
            }

            return textField;
        }

        @Override
        public void setTextField(String str) {
            String fixed = str.replaceAll("[^0-9]", "");
            if (str.startsWith("-")) {
                fixed = "-" + fixed;
            }

            this.textField = fixed;

            try {
                setNumber(Integer.parseInt(str));
            } catch (Exception e) {}
        }

        public void setNumber(int parseInt) {
            set(parseInt);
        }
    }

    public static class DoubleFieldHelper extends IntegerFieldHelper {
        public DoubleFieldHelper(String f, Object o) {
            super(f, o);
        }

        @Override
        public String getTextField() {
            if (textField == null) {
                textField = "" + getDouble();
            }

            return textField;
        }

        @Override
        public void setTextField(String str) {
            String fixed = str.replaceAll("[^0-9.]", "");
            if (str.startsWith("-")) {
                fixed = "-" + fixed;
            }

            this.textField = fixed;

            try {
                set(Double.parseDouble(str));
            } catch (Exception e) {}
        }
    }
    
    public static class FloatFieldHelper extends IntegerFieldHelper {
        public FloatFieldHelper(String f, Object o) {
            super(f, o);
        }

        @Override
        public String getTextField() {
            if (textField == null) {
                textField = "" + getFloat();
            }

            return textField;
        }

        @Override
        public void setTextField(String str) {
            String fixed = str.replaceAll("[^0-9.]", "");
            if (str.startsWith("-")) {
                fixed = "-" + fixed;
            }

            this.textField = fixed;

            try {
                set(Float.parseFloat(str));
            } catch (Exception e) {}
        }
    }
    
    public static class ItemAmountFieldHelper extends IntegerFieldHelper {
        public ItemAmountFieldHelper(String f, ItemField item) {
            super(f, item);
        }
        
        @Override
        public String getTextField() {
            if (textField == null) {
                textField = "" + ((ItemField)o).getStack().stackSize;
            }

            return textField;
        }
        
        @Override
        public void setTextField(String str) {
            if (str.contains("-")) {
                str = str.replace("-", "");
            }
            
            super.setTextField(str);
        }
        
        @Override
        public void setNumber(int parseInt) {
            if (parseInt < 1) {
                parseInt = 1;
                textField = "1";
            }
            
            super.setNumber(parseInt);
            ItemStack stack = ((ItemField)o).getStack();
            stack.stackSize = parseInt;
            ((ItemField)o).setItemStack(stack);
        }
    }
}
