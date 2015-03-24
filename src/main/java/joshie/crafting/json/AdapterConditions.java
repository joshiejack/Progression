package joshie.crafting.json;

import java.lang.reflect.Type;

import joshie.crafting.api.CraftingAPI;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class AdapterConditions implements JsonDeserializer<DataCondition>, JsonSerializer<DataCondition> {
	@Override
	public DataCondition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject data = json.getAsJsonObject();
		String name = data.get("Unique Name").getAsString();
		String type = data.get("Type").getAsString();		
		return new DataCondition(type, name, data);
	}

	@Override
	public JsonElement serialize(DataCondition src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject json = new JsonObject();
		json.addProperty("Type", src.type);
		json.addProperty("Unique Name", src.name);
		CraftingAPI.registry.getCondition(src.type, src.name, src.data).serialize(json);
		return json;
	}
}
