package joshie.progression.helpers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import joshie.progression.api.IFieldProvider;
import joshie.progression.api.IInitAfterRead;
import joshie.progression.api.IFilter;
import joshie.progression.api.ISpecialJSON;
import joshie.progression.handlers.APIHandler;
import joshie.progression.lib.ProgressionInfo;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class JSONHelper {
    public static boolean getExists(JsonObject data, String string) {
        return data.get(string) != null;
    }

    public static Enum getEnum(JsonObject data, String string, Enum default_) {
        if (data.get(string) != null) {
            try {
                return Enum.valueOf(default_.getClass(), getString(data, "enum:" + string, default_.name()));
            } catch (Exception e) {}
        }

        return default_;
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
            ItemStack ret = stack == null ? default_ : stack;
        }

        return default_;
    }

    public static Item getItem(JsonObject data, String string, Item default_) {
        if (data.get(string) != null) {
            ResourceLocation deflt = Item.itemRegistry.getNameForObject(default_);
            String domain = getString(data, string + ":domain", deflt.getResourceDomain());
            String path = getString(data, string + ":path", deflt.getResourcePath());
            Item item = Item.itemRegistry.getObject(new ResourceLocation(domain, path));
            return item == null ? default_ : item;
        }

        return default_;
    }

    public static Block getBlock(JsonObject data, String string, Block default_) {
        if (data.get(string) != null) {
            ResourceLocation deflt = Block.blockRegistry.getNameForObject(default_);
            String domain = getString(data, string + ":domain", deflt.getResourceDomain());
            String path = getString(data, string + ":path", deflt.getResourcePath());
            Block block = Block.blockRegistry.getObject(new ResourceLocation(domain, path));
            return block == null ? default_ : block;
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

    public static void setEnum(JsonObject data, String string, Enum value, Enum default_) {
        if (value != null && !value.equals(default_)) {
            data.addProperty("enum:" + string, value.name());
        }
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

    public static void setItemStack(JsonObject data, String string, ItemStack value, ItemStack dflt) {
        if (value != null && !(dflt != null && value.getItem() == dflt.getItem() && value.getItemDamage() == dflt.getItemDamage())) {
            data.addProperty(string, StackHelper.getStringFromStack(value));
        }
    }

    public static void setItem(JsonObject data, String string, Item item, Item dflt) {
        if (item != null && item != dflt) {
            ResourceLocation location = Item.itemRegistry.getNameForObject(item);
            ResourceLocation deflt = Item.itemRegistry.getNameForObject(dflt);
            setString(data, string + ":domain", location.getResourceDomain(), deflt.getResourceDomain());
            setString(data, string + ":path", location.getResourcePath(), deflt.getResourcePath());
        }
    }

    public static void setBlock(JsonObject data, String string, Block block, Block dflt) {
        if (block != null && block != dflt) {
            ResourceLocation location = Block.blockRegistry.getNameForObject(block);
            ResourceLocation deflt = Block.blockRegistry.getNameForObject(dflt);
            setString(data, string + ":domain", location.getResourceDomain(), deflt.getResourceDomain());
            setString(data, string + ":path", location.getResourcePath(), deflt.getResourcePath());
        }
    }

    public static void setNBT(JsonObject data, String string, NBTTagCompound value, NBTTagCompound dflt) {
        if (value != null && !value.equals(dflt)) {
            data.addProperty(string, value.toString());
        }
    }

    public static List<IFilter> getItemFilters(JsonObject data, String name) {
        ArrayList<IFilter> filters = new ArrayList();
        if (data.get(name) == null) return filters;
        JsonArray array = data.get(name).getAsJsonArray();
        for (int i = 0; i < array.size(); i++) {
            JsonObject object = array.get(i).getAsJsonObject();
            String typeName = object.get("type").getAsString();
            JsonObject typeData = object.get("data").getAsJsonObject();
            IFilter filter = APIHandler.newItemFilter(typeName, typeData);
            if (filter != null) {
                filters.add(filter);
            }
        }

        return filters;
    }

    public static void setItemFilters(JsonObject data, String name, List<IFilter> filters) {
        JsonArray array = new JsonArray();
        for (IFilter filter : filters) {
            if (filter == null) continue;
            JsonObject object = new JsonObject();
            object.addProperty("type", filter.getUnlocalisedName());
            JsonObject typeData = new JsonObject();
            writeJSON(typeData, filter);
            object.add("data", typeData);
            array.add(object);
        }

        data.add(name, array);
    }

    private static void readEnum(JsonObject json, Field field, IFieldProvider object, Enum dflt) throws IllegalArgumentException, IllegalAccessException {
        field.set(object, getEnum(json, field.getName(), dflt));
    }

    private static void readBoolean(JsonObject json, Field field, IFieldProvider object, boolean dflt) throws IllegalArgumentException, IllegalAccessException {
        field.set(object, getBoolean(json, field.getName(), dflt));
    }

    private static void readString(JsonObject json, Field field, IFieldProvider object, String dflt) throws IllegalArgumentException, IllegalAccessException {
        field.set(object, getString(json, field.getName(), dflt));
    }

    private static void readInteger(JsonObject json, Field field, IFieldProvider object, int dflt) throws IllegalArgumentException, IllegalAccessException {
        field.set(object, getInteger(json, field.getName(), dflt));
    }

    private static void readFloat(JsonObject json, Field field, IFieldProvider object, float dflt) throws IllegalArgumentException, IllegalAccessException {
        field.set(object, getFloat(json, field.getName(), dflt));
    }

    private static void readDouble(JsonObject json, Field field, IFieldProvider object, double dflt) throws IllegalArgumentException, IllegalAccessException {
        field.set(object, getDouble(json, field.getName(), dflt));
    }

    private static void readItemFilters(JsonObject json, Field field, IFieldProvider object) throws IllegalArgumentException, IllegalAccessException {
        field.set(object, getItemFilters(json, field.getName()));
    }

    private static void readItemStack(JsonObject json, Field field, IFieldProvider object, ItemStack dflt) throws IllegalArgumentException, IllegalAccessException {
        field.set(object, getItemStack(json, field.getName(), dflt));
    }

    private static void readItem(JsonObject json, Field field, IFieldProvider object, Item dflt) throws IllegalArgumentException, IllegalAccessException {
        field.set(object, getItem(json, field.getName(), dflt));
    }

    private static void readBlock(JsonObject json, Field field, IFieldProvider object, Block dflt) throws IllegalArgumentException, IllegalAccessException {
        field.set(object, getBlock(json, field.getName(), dflt));
    }

    private static void readNBT(JsonObject json, Field field, IFieldProvider object, NBTTagCompound dflt) throws IllegalArgumentException, IllegalAccessException {
        field.set(object, getNBT(json, field.getName(), dflt));
    }

    public static void readVariables(JsonObject json, IFieldProvider provider) {
        try {
            for (Field field : provider.getClass().getFields()) {
                Object defaultValue = field.get(provider);
                if (field.getClass().isEnum()) readEnum(json, field, provider, (Enum) defaultValue);
                if (field.getType() == boolean.class) readBoolean(json, field, provider, (Boolean) defaultValue);
                if (field.getType() == String.class) readString(json, field, provider, (String) defaultValue);
                if (field.getType() == int.class) readInteger(json, field, provider, (Integer) defaultValue);
                if (field.getType() == float.class) readFloat(json, field, provider, (Float) defaultValue);
                if (field.getType() == double.class) readDouble(json, field, provider, (Double) defaultValue);
                if (field.getType() == ItemStack.class) readItemStack(json, field, provider, (ItemStack) defaultValue);
                if (field.getType() == Block.class) readBlock(json, field, provider, (Block) defaultValue);
                if (field.getType() == Item.class) readItem(json, field, provider, (Item) defaultValue);
                if (field.getType() == NBTTagCompound.class) readNBT(json, field, provider, (NBTTagCompound) defaultValue);
                if (field.getGenericType().toString().equals("java.util.List<" + ProgressionInfo.FILTER + ">")) readItemFilters(json, field, provider);
            }
        } catch (Exception e) {}
    }

    private static void writeEnum(JsonObject json, Field field, Object object, Enum dflt) throws IllegalArgumentException, IllegalAccessException {
        setEnum(json, field.getName(), (Enum) field.get(object), dflt);
    }

    private static void writeBoolean(JsonObject json, Field field, Object object, boolean dflt) throws IllegalArgumentException, IllegalAccessException {
        setBoolean(json, field.getName(), (Boolean) field.get(object), dflt);
    }

    private static void writeString(JsonObject json, Field field, Object object, String dflt) throws IllegalArgumentException, IllegalAccessException {
        setString(json, field.getName(), (String) field.get(object), dflt);
    }

    private static void writeInteger(JsonObject json, Field field, Object object, int dflt) throws IllegalArgumentException, IllegalAccessException {
        setInteger(json, field.getName(), (Integer) field.get(object), dflt);
    }

    private static void writeFloat(JsonObject json, Field field, Object object, float dflt) throws IllegalArgumentException, IllegalAccessException {
        setFloat(json, field.getName(), (Float) field.get(object), dflt);
    }

    private static void writeDouble(JsonObject json, Field field, Object object, double dflt) throws IllegalArgumentException, IllegalAccessException {
        setDouble(json, field.getName(), (Double) field.get(object), dflt);
    }

    private static void writeItemFilters(JsonObject json, Field field, Object object) throws IllegalArgumentException, IllegalAccessException {
        setItemFilters(json, field.getName(), (List<IFilter>) field.get(object));
    }

    private static void writeItemStack(JsonObject json, Field field, Object object, ItemStack dflt) throws IllegalArgumentException, IllegalAccessException {
        setItemStack(json, field.getName(), (ItemStack) field.get(object), dflt);
    }

    private static void writeItem(JsonObject json, Field field, Object object, Item dflt) throws IllegalArgumentException, IllegalAccessException {
        setItem(json, field.getName(), (Item) field.get(object), dflt);
    }

    private static void writeBlock(JsonObject json, Field field, Object object, Block dflt) throws IllegalArgumentException, IllegalAccessException {
        setBlock(json, field.getName(), (Block) field.get(object), dflt);
    }

    private static void writeNBT(JsonObject json, Field field, Object object, NBTTagCompound dflt) throws IllegalArgumentException, IllegalAccessException {
        setNBT(json, field.getName(), (NBTTagCompound) field.get(object), dflt);
    }

    public static void writeVariables(JsonObject json, IFieldProvider object) {
        try {
            for (Field field : object.getClass().getFields()) {
                Object defaultValue = field.get(APIHandler.getDefault(object));
                if (field.getClass().isEnum()) writeEnum(json, field, object, (Enum) defaultValue);
                if (field.getType() == boolean.class) writeBoolean(json, field, object, (Boolean) defaultValue);
                if (field.getType() == String.class) writeString(json, field, object, (String) defaultValue);
                if (field.getType() == int.class) writeInteger(json, field, object, (Integer) defaultValue);
                if (field.getType() == float.class) writeFloat(json, field, object, (Float) defaultValue);
                if (field.getType() == double.class) writeDouble(json, field, object, (Double) defaultValue);
                if (field.getType() == ItemStack.class) writeItemStack(json, field, object, (ItemStack) defaultValue);
                if (field.getType() == Block.class) writeBlock(json, field, object, (Block) defaultValue);
                if (field.getType() == Item.class) writeItem(json, field, object, (Item) defaultValue);
                if (field.getType() == NBTTagCompound.class) writeNBT(json, field, object, (NBTTagCompound) defaultValue);
                if (field.getGenericType().toString().equals("java.util.List<" + ProgressionInfo.FILTER + ">")) writeItemFilters(json, field, object);
            }
        } catch (Exception e) {}
    }

    public static void readJSON(JsonObject data, IFieldProvider provider) {
        boolean specialOnly = false;
        if (provider instanceof ISpecialJSON) {
            ISpecialJSON special = ((ISpecialJSON) provider);
            special.readFromJSON(data);
            specialOnly = special.onlySpecial();
        }

        if (!specialOnly) JSONHelper.readVariables(data, provider);
        if (provider instanceof IInitAfterRead) {
            ((IInitAfterRead) provider).init();
        }
    }

    public static void writeJSON(JsonObject data, IFieldProvider provider) {
        boolean specialOnly = false;
        if (provider instanceof ISpecialJSON) {
            ISpecialJSON special = ((ISpecialJSON) provider);
            special.writeToJSON(data);
            specialOnly = special.onlySpecial();
        }

        if (!specialOnly) JSONHelper.writeVariables(data, provider);
    }
}
