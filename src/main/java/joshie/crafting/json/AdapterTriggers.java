package joshie.crafting.json;

import java.lang.reflect.Type;

import joshie.crafting.api.CraftingAPI;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class AdapterTriggers implements JsonDeserializer<DataTrigger>, JsonSerializer<DataTrigger> {
	@Override
	public DataTrigger deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject data = json.getAsJsonObject();
		String name = data.get("Unique Name").getAsString();
		String type = data.get("Type").getAsString();
		boolean repeatable = false;
		if (data.get("Repeatable") != null) {
			repeatable = data.get("Repeatable").getAsBoolean();
		}
		
		String[] conditions = new String[] {};
		
		if (data.get("Conditions") != null) {
			JsonArray array = data.get("Conditions").getAsJsonArray();
			conditions = new String[array.size()];
			for (int i = 0; i < conditions.length; i++) {
				conditions[i] = array.get(i).getAsString();
			}
		}
		
		return new DataTrigger(type, name, data, conditions);
	}

	@Override
	public JsonElement serialize(DataTrigger src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject json = new JsonObject();
		json.addProperty("Type", src.type);
		json.addProperty("Unique Name", src.name);		
		JsonArray array = new JsonArray();
		if (src.conditions != null && src.conditions.length > 0) {
			for (String s: src.conditions) {
				array.add(new JsonPrimitive(s));
			}
			
			json.add("Conditions", array);
		}
		
		CraftingAPI.registry.getTrigger(src.type, src.name, src.data).serialize(json);
		return json;
	}
}
