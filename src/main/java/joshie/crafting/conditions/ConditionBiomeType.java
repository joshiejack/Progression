package joshie.crafting.conditions;

import java.util.UUID;

import joshie.crafting.gui.fields.ICallback;
import joshie.crafting.gui.fields.TextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class ConditionBiomeType extends ConditionBase implements ICallback {
    private Type[] theBiomeTypes = new Type[] { Type.FOREST };
    public String biomeTypes = "forest";

    public ConditionBiomeType() {
        super("biomeType", 0xFF00B200);
        list.add(new TextField("biomeTypes", this));
    }

    private Type getBiomeType(String string) {
        for (Type t : Type.values()) {
            if (t.name().equalsIgnoreCase(string)) return t;
        }

        return Type.FOREST;
    }

    @Override
    public boolean isSatisfied(World world, EntityPlayer player, UUID uuid) {
        if (player == null) return false;
        Type types[] = BiomeDictionary.getTypesForBiome(world.getBiomeGenForCoords((int) player.posX, (int) player.posZ));
        for (Type type : theBiomeTypes) {
            for (Type compare : types) {
                if (compare == type) return true;
            }
        }

        return false;
    }

    @Override
    public void readFromJSON(JsonObject data) {
        ConditionBiomeType condition = new ConditionBiomeType();
        JsonArray array = data.get("types").getAsJsonArray();
        Type[] types = new Type[array.size()];
        for (int i = 0; i < types.length; i++) {
            types[i] = getBiomeType(array.get(i).getAsString());
        }

        theBiomeTypes = types;
    }

    @Override
    public void writeToJSON(JsonObject data) {
        JsonArray array = new JsonArray();
        for (Type t : theBiomeTypes) {
            array.add(new JsonPrimitive(t.name().toLowerCase()));
        }

        data.add("types", array);
    }

    public Type getTypeFromName(String name) {
        for (Type type : Type.values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }

        return Type.FOREST;
    }

    @Override
    public void setField(String str) {
        String[] split = str.split(",");
        StringBuilder fullString = new StringBuilder();
        try {
            Type[] types = new Type[split.length];
            for (int i = 0; i < types.length; i++) {
                types[i] = getTypeFromName(split[i].trim());
            }

            theBiomeTypes = types;
        } catch (Exception e) {}

        biomeTypes = str;
    }
}
