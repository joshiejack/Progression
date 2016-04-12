package joshie.progression.gui.fields;

import joshie.progression.api.gui.Position;
import joshie.progression.api.special.IInit;
import joshie.progression.api.special.ISetterCallback;
import joshie.progression.gui.editors.ITextEditable;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Field;

import static joshie.progression.gui.core.GuiList.TEXT_EDITOR_FULL;

public class FieldHelper implements ITextEditable {
    public Position type;
    public Object o;
    public Field f;

    public FieldHelper() {}

    public FieldHelper(String f, Object o, Position type) {
        this.o = o;

        try {
            this.f = o.getClass().getField(f);
            this.type = type;
        } catch (Exception e) {}
    }

    public void select() {
        TEXT_EDITOR_FULL.select(this, type);
    }

    public String getText() {
        return TEXT_EDITOR_FULL.getText(this);
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

            //Init the object after we've set it
            if (o instanceof IInit) {
                ((IInit) o).init(true);
            }
        } catch (Exception e) {}
    }

    public String getFieldName() {
        return f.getName();
    }

    @Override
    public String getTextField() {
        return getString();
    }

    @Override
    public void setTextField(String str) {
        if (o instanceof ISetterCallback) {
            if (!((ISetterCallback) o).setField(f.getName(), str)) {
                set(str);
            }
        } else set(str);
    }

    public static class IntegerFieldHelper extends FieldHelper {
        protected String textField = null;

        public IntegerFieldHelper() {}

        public IntegerFieldHelper(String f, Object o, Position type) {
            super(f, o, type);
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
        public DoubleFieldHelper(String f, Object o, Position type) {
            super(f, o, type);
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
        public FloatFieldHelper(String f, Object o, Position type) {
            super(f, o, type);
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
        public ItemAmountFieldHelper(String f, ItemField item, Position type) {
            super(f, item, type);
        }

        @Override
        public String getTextField() {
            if (textField == null) {
                textField = "" + ((ItemField) o).getStack().stackSize;
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
            ItemStack stack = ((ItemField) o).getStack();
            stack.stackSize = parseInt;
            ((ItemField) o).setObject(stack.copy());
        }
    }
}
