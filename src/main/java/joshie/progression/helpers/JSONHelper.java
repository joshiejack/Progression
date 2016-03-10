package joshie.progression.helpers;

import java.util.HashSet;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import joshie.progression.api.IEntityFilter;
import joshie.progression.api.IItemFilter;
import joshie.progression.handlers.APIHandler;
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

    public static Set<IItemFilter> getItemFilters(JsonObject data, String name) {
        HashSet<IItemFilter> filters = new HashSet();
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

    public static void setItemFilters(JsonObject data, String name, Set<IItemFilter> filters) {
        JsonArray array = new JsonArray();
        for (IItemFilter filter: filters) {
            if (filter == null) continue;
            JsonObject object = new JsonObject();
            object.addProperty("type", filter.getName());
            JsonObject typeData = new JsonObject();
            filter.writeToJSON(typeData);
            object.add("data", typeData);
            array.add(object);
        }
        
        data.add(name, array);
    }
    
    public static Set<IEntityFilter> getEntityFilters(JsonObject data, String name) {
        HashSet<IEntityFilter> filters = new HashSet();
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

    public static void setEntityFilters(JsonObject data, String name, Set<IEntityFilter> filters) {
        JsonArray array = new JsonArray();
        for (IEntityFilter filter: filters) {
            if (filter == null) continue;
            JsonObject object = new JsonObject();
            object.addProperty("type", filter.getName());
            JsonObject typeData = new JsonObject();
            filter.writeToJSON(typeData);
            object.add("data", typeData);
            array.add(object);
        }
        
        data.add(name, array);
    }
}
