package joshie.crafting.json;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICondition;
import joshie.crafting.helpers.StackHelper;
import joshie.crafting.lib.CraftingInfo;
import net.minecraft.item.ItemStack;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONLoader {
	private static interface IDefault {
		public List getList();
		public IDefault setDefaults();
	}
	
	public static class ConditionList implements IDefault {
		private List<Conditions> conditions = new ArrayList();
		
		@Override
		public List getList() {
			return conditions;
		}
		
		@Override
		public IDefault setDefaults() {
			Conditions condition = new Conditions();
			condition.item = "minecraft:furnace";
			condition.reqType = "technology";
			condition.reqData = "Heat's Up";
			condition.craftType = "crafting";
			conditions.add(condition);
			
			Conditions condition2 = new Conditions();
			condition2.item = "minecraft:iron_ingot";
			condition2.reqType = "technology";
			condition2.reqData = "Iron Heights";
			condition2.craftType = "furnace";
			conditions.add(condition2);
			return this;
		} //Used to load in some test data
	}
	
	public static class TechList implements IDefault {
		private List<Technology> technologies = new ArrayList();
		
		@Override
		public List getList() {
			return technologies;
		}
		
		@Override
		public IDefault setDefaults() {
			Technology furnace = new Technology();
			furnace.name = "Heat's Up";
			technologies.add(furnace);
			
			Technology iron = new Technology();
			iron.name = "Iron Heights";
			technologies.add(iron);
			return this;
		} //Used to load in some test data
	}
	
	private static IDefault loadJSON(Gson gson, Class clazz, String field) {
		try {
			IDefault ret = null;
			File dir = new File("config" + File.separator + CraftingInfo.MODPATH);
			if (!dir.exists()) {
				dir.mkdir();
			}
			
			File file = new File("config" + File.separator + CraftingInfo.MODPATH + File.separator + field + ".json");
			if (!file.exists()) {
				ret = ((IDefault) clazz.newInstance()).setDefaults();
				Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
				writer.write(gson.toJson(ret));
				writer.close();
				return ret;
			} else return gson.fromJson(FileUtils.readFileToString(file), clazz);
		} catch (Exception e) { e.printStackTrace(); }
		
		//Return a dummy version
		return new IDefault() {
			@Override
			public List getList() {
				return new ArrayList();
			}

			@Override
			public IDefault setDefaults() {
				return null;
			}
		};
	}
	
	public static void loadDataFromJSON() {
		GsonBuilder builder = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping();
		Gson gson = builder.create();
		
		IDefault technologies = loadJSON(gson, TechList.class, "technologies");
		IDefault conditions = loadJSON(gson, ConditionList.class, "conditions");
		
		//Register all the technologies
		for (Technology tech: (List<Technology>)technologies.getList()) {
			CraftingAPI.tech.register(tech);
		}
		
		//Add new conditions for an item
		for (Conditions c: (List<Conditions>)conditions.getList()) {
			ICondition condition = CraftingAPI.conditions.getConditionFromName(c.reqType);
			ICondition clone = condition.newInstance(c.reqData).setCraftingType(c.craftType);
			ItemStack stack = StackHelper.getStackFromString(c.item);
			CraftingAPI.conditions.addCondition(stack, clone);
		}
		
		conditions.getList().clear();
		technologies.getList().clear();
		conditions = null;
		technologies = null;
	}
}
