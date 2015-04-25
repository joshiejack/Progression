package joshie.crafting.helpers;

import net.minecraft.item.ItemStack;

import com.google.gson.JsonObject;

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
            return StackHelper.getStackFromString(name);
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
}
