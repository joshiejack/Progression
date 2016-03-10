 package joshie.progression.criteria.filters;

import com.google.gson.JsonObject;

import joshie.progression.gui.fields.ICallback;
import joshie.progression.gui.fields.TextField;
import joshie.progression.helpers.JSONHelper;
import joshie.progression.helpers.StackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class FilterNBT extends FilterBase implements ICallback {
    public NBTTagCompound tagValue = new NBTTagCompound();
    public String tagText;

    public FilterNBT() {
        super("nbtString");
        list.add(new TextField("tagText", this));
    }

    @Override
    public void readFromJSON(JsonObject data) {
    	tagValue = JSONHelper.getNBT(data, "tagValue", tagValue);
    	tagText = tagValue.toString();
    }

    @Override
    public void writeToJSON(JsonObject data) {
        JSONHelper.setNBT(data, "tagValue", tagValue);
    }

    @Override
    public boolean matches(ItemStack check) { //TODO: Add Partial matching
    	return check.hasTagCompound() && check.getTagCompound().equals(tagValue);
    }

	@Override
	public boolean setField(String fieldName, String fieldValue) {
		tagValue = StackHelper.getTag(new String[] { fieldValue }, 0);
		tagText = fieldValue; //Temporary field
		return false;
	}
}
