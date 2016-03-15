package joshie.progression.helpers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import joshie.progression.api.IEntityFilter;
import joshie.progression.api.IFieldProvider;
import joshie.progression.api.IItemFilter;
import joshie.progression.handlers.APIHandler;
import joshie.progression.lib.ProgressionInfo;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class JSONHelper {
    public static boolean getExists(JsonObject data, String string) {
        return data.get(string) != null;
    }

    public static boolean getBoolean(JsonObject data, String string, boolean default_) {
        if (data.get(string) != null) {
            return data.get(string).getAsBoolean();
        }

        return default_;
    }

    public static int getInteger(JsonObject data, String string, int default_) {
        if (data.get(string) != null) {
            return data.get(string).getAsInt();
        }

        return default_;
    }

    public static float getFloat(JsonObject data, String string, float default_) {
        if (data.get(string) != null) {
            return data.get(string).getAsFloat();
        }

        return default_;
    }

    public static double getDouble(JsonObject data, String string, double default_) {
        if (data.get(string) != null) {
            return data.get(string).getAsDouble();
        }

        return default_;
    }

    public static String getString(JsonObject data, String string, String default_) {
        if (data.get(string) != null) {
            return data.get(string).getAsString();
        }

        return default_;
    }

    public static ItemStack getItemStack(JsonObject data, String string, ItemStack default_) {
        if (data.get(string) != null) {
            String name = data.get(string).getAsString();
            ItemStack stack = StackHelper.getStackFromString(name);
            return stack == null ? default_ : stack;
        }

        return default_;
    }

    public static Item getItem(JsonObject data, String string, Item default_) {
        if (data.get(string) != null) {
            String name = data.get(string).getAsString();
            Item item = StackHelper.getItemByText(name);
            return item == null ? default_ : item;
        }

        return default_;
    }

    public static NBTTagCompound getNBT(JsonObject data, String string, NBTTagCompound default_) {
        if (data.get(string) != null) {
            String name = data.get(string).getAsString();
            NBTTagCompound tag = StackHelper.getTag(new String[] { string }, 0);
            return tag == null ? default_ : tag;
        }

        return default_;
    }

    public static void setBoolean(JsonObject data, String string, boolean value, boolean not) {
        if (value != not) {
            data.addProperty(string, value);
        }
    }

    public static void setInteger(JsonObject data, String string, int value, int not) {
        if (value != not) {
            data.addProperty(string, value);
        }
    }

    public static void setFloat(JsonObject data, String string, float value, float not) {
        if (value != not) {
            data.addProperty(string, value);
        }
    }

    public static void setDouble(JsonObject data, String string, double value, double not) {
        if (value != not) {
            data.addProperty(string, value);
        }
    }

    public static void setString(JsonObject data, String string, String value, String not) {
        if (!value.equals(not)) {
            data.addProperty(string, value);
        }
    }

    public static void setItemStack(JsonObject data, String string, ItemStack value) {
        if (value != null) {
            data.addProperty(string, StackHelper.getStringFromStack(value));
        }
    }

    public static void setItem(JsonObject data, String string, Item value) {
        if (value != null) {
            data.addProperty(string, StackHelper.getStringFromObject(value));
        }
    }

    public static void setNBT(JsonObject data, String string, NBTTagCompound value) {
        if (value != null) {
            data.addProperty(string, value.toString());
        }
    }

    public static List<IItemFilter> getItemFilters(JsonObject data, String name) {
        ArrayList<IItemFilter> filters = new ArrayList();
        if (data.get(name) == null) return filters;
        JsonArray array = data.get(name).getAsJsonArray();
        for (int i = 0; i < array.size(); i++) {
            JsonObject object = array.get(i).getAsJsonObject();
            String typeName = object.get("type").getAsString();
            JsonObject typeData = object.get("data").getAsJsonObject();
            IItemFilter filter = APIHandler.newItemFilter(typeName, typeData);
            if (filter != null) {
                filters.add(filter);
            }
        }

        return filters;
    }

    public static void setItemFilters(JsonObject data, String name, List<IItemFilter> filters) {
        JsonArray array = new JsonArray();
        for (IItemFilter filter : filters) {
            if (filter == null) continue;
            JsonObject object = new JsonObject();
            object.addProperty("type", filter.getUnlocalisedName());
            JsonObject typeData = new JsonObject();
            filter.writeToJSON(typeData);
            object.add("data", typeData);
            array.add(object);
        }

        data.add(name, array);
    }

    public static List<IEntityFilter> getEntityFilters(JsonObject data, String name) {
        ArrayList<IEntityFilter> filters = new ArrayList();
        if (data.get(name) == null) return filters;
        JsonArray array = data.get(name).getAsJsonArray();
        for (int i = 0; i < array.size(); i++) {
            JsonObject object = array.get(i).getAsJsonObject();
            String typeName = object.get("type").getAsString();
            JsonObject typeData = object.get("data").getAsJsonObject();
            IEntityFilter filter = APIHandler.newEntityFilter(typeName, typeData);
            if (filter != null) {
                filters.add(filter);
            }
        }

        return filters;
    }

    public static void setEntityFilters(JsonObject data, String name, List<IEntityFilter> filters) {
        JsonArray array = new JsonArray();
        for (IEntityFilter filter : filters) {
            if (filter == null) continue;
            JsonObject object = new JsonObject();
            object.addProperty("type", filter.getUnlocalisedName());
            JsonObject typeData = new JsonObject();
            filter.writeToJSON(typeData);
            object.add("data", typeData);
            array.add(object);
        }

        data.add(name, array);
    }

    private static void readBoolean(JsonObject json, Field field, Object object, boolean dflt) throws IllegalArgumentException, IllegalAccessException {
        field.set(object, getBoolean(json, field.getName(), dflt));
    }

    private static void readString(JsonObject json, Field field, Object object, String dflt) throws IllegalArgumentException, IllegalAccessException {
        field.set(object, getString(json, field.getName(), dflt));
    }

    private static void readInteger(JsonObject json, Field field, Object object, int dflt) throws IllegalArgumentException, IllegalAccessException {
        field.set(object, getInteger(json, field.getName(), dflt));
    }

    private static void readFloat(JsonObject json, Field field, Object object, float dflt) throws IllegalArgumentException, IllegalAccessException {
        field.set(object, getFloat(json, field.getName(), dflt));
    }

    private static void readItemFilters(JsonObject json, Field field, Object object) throws IllegalArgumentException, IllegalAccessException {
        field.set(object, getItemFilters(json, field.getName()));
    }

    private static void readEntityFilters(JsonObject json, Field field, Object object) throws IllegalArgumentException, IllegalAccessException {
        field.set(object, getEntityFilters(json, field.getName()));
    }

    public static void readVariables(JsonObject json, Object object) {
        try {
            for (Field field : object.getClass().getFields()) {
                Object defaultValue = field.get(object);
                if (field.getType() == boolean.class) readBoolean(json, field, object, (Boolean) defaultValue);
                if (field.getType() == String.class) readString(json, field, object, (String) defaultValue);
                if (field.getType() == int.class) readInteger(json, field, object, (Integer) defaultValue);
                if (field.getType() == float.class) readFloat(json, field, object, (Float) defaultValue);
                if (field.getGenericType().toString().equals("java.util.List<" + ProgressionInfo.ITEMFILTER + ">")) readItemFilters(json, field, object);
                if (field.getGenericType().toString().equals("java.util.List<" + ProgressionInfo.ENTITYFILTER + ">")) readEntityFilters(json, field, object);
            }
        } catch (Exception e) {}
    }
    
    private static void writeBoolean(JsonObject json, Field field, Object object, boolean dflt) throws IllegalArgumentException, IllegalAccessException {
        JSONHelper.setBoolean(json, field.getName(), (Boolean) field.get(object), dflt);
    }

    private static void writeString(JsonObject json, Field field, Object object, String dflt) throws IllegalArgumentException, IllegalAccessException {
        JSONHelper.setString(json, field.getName(), (String) field.get(object), dflt);
    }

    private static void writeInteger(JsonObject json, Field field, Object object, int dflt) throws IllegalArgumentException, IllegalAccessException {
        JSONHelper.setInteger(json, field.getName(), (Integer) field.get(object), dflt);
    }

    private static void writeFloat(JsonObject json, Field field, Object object, float dflt) throws IllegalArgumentException, IllegalAccessException {
        JSONHelper.setFloat(json, field.getName(), (Float) field.get(object), dflt);
    }

    private static void writeItemFilters(JsonObject json, Field field, Object object) throws IllegalArgumentException, IllegalAccessException {
        JSONHelper.setItemFilters(json, field.getName(), (List<IItemFilter>) field.get(object));
    }

    private static void writeEntityFilters(JsonObject json, Field field, Object object) throws IllegalArgumentException, IllegalAccessException {
        JSONHelper.setEntityFilters(json, field.getName(), (List<IEntityFilter>) field.get(object));
    }

    public static void writeVariables(JsonObject json, IFieldProvider object) {
        try {
            for (Field field : object.getClass().getFields()) {
                Object defaultValue = field.get(APIHandler.getDefault(object));
                if (field.getType() == boolean.class) writeBoolean(json, field, object, (Boolean) defaultValue);
                if (field.getType() == String.class) writeString(json, field, object, (String) defaultValue);
                if (field.getType() == int.class) writeInteger(json, field, object, (Integer) defaultValue);
                if (field.getType() == float.class) writeFloat(json, field, object, (Float) defaultValue);
                if (field.getGenericType().toString().equals("java.util.List<" + ProgressionInfo.ITEMFILTER + ">")) writeItemFilters(json, field, object);
                if (field.getGenericType().toString().equals("java.util.List<" + ProgressionInfo.ENTITYFILTER + ">")) writeEntityFilters(json, field, object);
            }
        } catch (Exception e) {}
    }
}
