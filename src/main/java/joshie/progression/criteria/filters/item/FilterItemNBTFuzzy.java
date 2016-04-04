package joshie.progression.criteria.filters.item;

import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.IEnum;
import joshie.progression.api.special.IInit;
import joshie.progression.api.special.ISetterCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

@ProgressionRule(name="tagsingle", color=0xFF00B2B2)
public class FilterItemNBTFuzzy extends FilterBaseItem implements IInit, ISetterCallback, IEnum {
    public String name = "";
    public String value = "";
    public NBTType type = NBTType.STRING;

    @Override
    public boolean matches(ItemStack check) { //TODO: Add Partial matching
        if (!check.hasTagCompound()) return  false;
        NBTTagCompound tag = check.getTagCompound();
        switch (type) {
            case BYTE: return tag.getByte(name) == bytevalue;
            case SHORT: return tag.getShort(name) == bytevalue;
            case INT: return tag.getInteger(name) == bytevalue;
            case LONG: return tag.getLong(name) == bytevalue;
            case FLOAT: return tag.getFloat(name) == bytevalue;
            case DOUBLE: return tag.getDouble(name) == bytevalue;
            case STRING: return tag.getString(name).equals(value);
            default: return false;
        }
    }

    public transient byte bytevalue = 0;
    public void parseByte() {
        try {
            bytevalue = Byte.parseByte(value);
        } catch (Exception e) {}
    }

    public transient short shortvalue = 0;
    public void parseShort() {
        try {
            shortvalue = Short.parseShort(value);
        } catch (Exception e) {}
    }

    public transient int intvalue = 0;
    public void parseInt() {
        try {
            intvalue = Integer.parseInt(value);
        } catch (Exception e) {}
    }

    public transient long longvalue = 0;
    public void parseLong() {
        try {
            longvalue = Long.parseLong(value);
        } catch (Exception e) {}
    }

    public transient float floatvalue = 0;
    public void parseFloat() {
        try {
            floatvalue = Float.parseFloat(value);
        } catch (Exception e) {}
    }

    public transient double doublevalue = 0;
    public void parseDouble() {
        try {
            doublevalue = Double.parseDouble(value);
        } catch (Exception e) {}
    }

    @Override
    public void init() {
        switch (type) {
            case BYTE: parseByte();
                break;
            case SHORT: parseShort();
                break;
            case INT: parseInt();
                break;
            case LONG: parseLong();
                break;
            case FLOAT: parseFloat();
                break;
            case DOUBLE: parseDouble();
                break;
        }
    }

    @Override
    public boolean setField(String fieldName, Object object) {
        if (fieldName.equals(value)) {
            init(); //PARSE
            return true;
        }

        return false;
    }

    @Override
    public Enum next(String name) {
        int id = type.ordinal() + 1;
        if (id < NBTType.values().length) {
            return NBTType.values()[id];
        }

        return NBTType.values()[0];
    }

    @Override
    public boolean isEnum(String name) {
        return name.equals("type");
    }

    public static enum NBTType {
         BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, STRING;
    }
}
