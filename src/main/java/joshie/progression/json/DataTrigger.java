package joshie.progression.json;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataTrigger extends DataGeneric {
	List<DataGeneric> conditions;
	
	public DataTrigger() {}
	public DataTrigger(UUID uuid, String type, JsonObject data, ArrayList<DataGeneric> theConditions) {
		super(uuid, type, data);
		this.conditions = theConditions;
	}
}
