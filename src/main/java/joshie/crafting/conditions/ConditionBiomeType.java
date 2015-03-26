package joshie.crafting.conditions;

import java.util.UUID;

import joshie.crafting.api.ICondition;
import joshie.crafting.plugins.minetweaker.Conditions;
import minetweaker.MineTweakerAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

@ZenClass("mods.craftcontrol.conditions.BiomeType")
public class ConditionBiomeType extends ConditionBase {
	private Type[] biomeTypes;
	
	public ConditionBiomeType() {
		super("biomeType");
	}
	
	@ZenMethod
	public void add(String unique, String[] biomeTypes) {
		ConditionBiomeType condition = new ConditionBiomeType();
		Type[] types = new Type[biomeTypes.length];
		for (int i = 0; i < biomeTypes.length; i++) {
			types[i] = getBiomeType(biomeTypes[i]);
		}

		condition.biomeTypes = types;
		MineTweakerAPI.apply(new Conditions(unique, condition));
	}

	private Type getBiomeType(String string) {
		for (Type t: Type.values()) {
			if (t.name().equalsIgnoreCase(string)) return t;
		}
		
		return Type.FOREST;
	}

	@Override
	public boolean isSatisfied(World world, EntityPlayer player, UUID uuid) {
		if (player == null) return false;
		Type types[] = BiomeDictionary.getTypesForBiome(world.getBiomeGenForCoords((int)player.posX, (int)player.posZ));
		for (Type type: biomeTypes) {
			for (Type compare: types) {
				if (compare == type) return true;
			}
		}
		
		return false;
	}

	@Override
	public ICondition deserialize(JsonObject data) {
		ConditionBiomeType condition = new ConditionBiomeType();
		JsonArray array = data.get("types").getAsJsonArray();
		Type[] types = new Type[array.size()];
		for (int i = 0; i < types.length; i++) {
			types[i] = getBiomeType(array.get(i).getAsString());
		}
		
		condition.biomeTypes = types;
		return condition;
	}

	@Override
	public void serialize(JsonObject elements) {
		JsonArray array = new JsonArray();
		for (Type t: biomeTypes) {
			array.add(new JsonPrimitive(t.name().toLowerCase()));
		}
			
		elements.add("types", array);
	}
}
