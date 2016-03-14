 package joshie.progression.criteria.filters;

import com.google.gson.JsonObject;

import joshie.progression.gui.fields.ISetterCallback;
import joshie.progression.gui.fields.TextField;
import joshie.progression.helpers.JSONHelper;
import joshie.progression.helpers.StackHelper;
import joshie.progression.lib.SafeStack;
import joshie.progression.lib.SafeStack.SafeStackNBTOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class FilterNBT extends FilterBase implements ISetterCallback {
    public NBTTagCompound tagValue = new NBTTagCompound();
    public String tagText = "";

    public FilterNBT() {
        super("nbtString", 0xFF00B2B2);
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
