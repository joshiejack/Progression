package joshie.progression.criteria.conditions;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import joshie.progression.Progression;
import joshie.progression.api.IPlayerTeam;
import joshie.progression.api.special.ISetterCallback;
import joshie.progression.api.special.ISpecialJSON;
import joshie.progression.items.ItemCriteria;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class ConditionBiomeType extends ConditionBase implements ISetterCallback, ISpecialJSON {
    private Type[] theBiomeTypes = new Type[] { Type.FOREST };
    public String biomeTypes = "forest";

    public ConditionBiomeType() {
        super(ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.ifIsBiome), "biomeType", 0xFF00B200);
    }

    @Override
    public boolean isSatisfied(IPlayerTeam team) {
        for (EntityPlayer player: team.getTeamEntities()) { //If any team member has the achievement
            Type types[] = BiomeDictionary.getTypesForBiome(player.worldObj.getBiomeGenForCoords(new BlockPos(player)));
            for (Type type : theBiomeTypes) {
                for (Type compare : types) {
                    if (compare == type) return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean onlySpecial() {
        return true;
    }

    @Override
    public String getConditionDescription() {
        if (inverted) return Progression.format(getUnlocalisedName() + ".description.inverted", biomeTypes);
        return Progression.format(getUnlocalisedName() + ".description", biomeTypes);
    }

    @Override
    public void readFromJSON(JsonObject data) {
        ConditionBiomeType condition = new ConditionBiomeType();
        JsonArray array = data.get("types").getAsJsonArray();
        Type[] types = new Type[array.size()];
        for (int i = 0; i < types.length; i++) {
            types[i] = Type.getType(array.get(i).getAsString());
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

    @Override
    public boolean setField(String fieldName, Object object) {
        String fieldValue = (String) object;
        String[] split = fieldValue.split(",");
        StringBuilder fullString = new StringBuilder();
        try {
            Type[] types = new Type[split.length];
            for (int i = 0; i < types.length; i++) {
                types[i] = Type.getType(split[i].trim());
            }

            theBiomeTypes = types;
        } catch (Exception e) {}

        biomeTypes = fieldValue;
        return true;
    }
}
