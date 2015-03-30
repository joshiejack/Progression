package joshie.crafting.json;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

public class DataTrigger extends DataGeneric {
	List<DataGeneric> conditions;
	
	public DataTrigger() {}
	public DataTrigger(String type, JsonObject data, ArrayList<DataGeneric> theConditions) {
		super(type, data);
		this.conditions = theConditions;
	}
}
