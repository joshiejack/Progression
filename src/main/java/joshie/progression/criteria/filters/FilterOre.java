package joshie.progression.criteria.filters;

import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.gson.JsonObject;

import joshie.progression.gui.fields.ISetterCallback;
import joshie.progression.gui.fields.TextField;
import joshie.progression.helpers.JSONHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class FilterOre extends FilterBase implements ISetterCallback {
    public static HashMultimap<String, String> cache = HashMultimap.create();
    public String oreName = "ingotIron";
    public String checkName;
    public boolean matchBoth;
    public boolean matchFront;
    public boolean matchBack;

    public FilterOre() {
        super("oreDictionary", 0xFFB25900);
        list.add(new TextField("oreName", this));
    }

    @Override
    public void readFromJSON(JsonObject data) {
        oreName = JSONHelper.getString(data, "oreName", "ingotIron");
        setField("oreName", oreName);
    }

    @Override
    public void writeToJSON(JsonObject data) {
        JSONHelper.setString(data, "oreName", oreName, "ingotIron");
    }

    @Override
    public boolean matches(ItemStack check) {
        //Build the key
        String key = Item.itemRegistry.getNameForObject(check.getItem()) + " " + check.getItemDamage();
        
        Set<String> names = null;
        //Attempt to get the ore names from the cache
        if (cache.containsKey(key)) names = cache.get(key);
        else {
            int[] ores = OreDictionary.getOreIDs(check);
            for (int i : ores) {
                cache.put(key, OreDictionary.getOreName(i));
            }

            names = cache.get(key);
        }

        for (String itemName : names) {
            if (matchBoth && itemName.contains(checkName)) return true;
            else if (matchFront && !matchBack && itemName.endsWith(checkName)) return true;
            else if (!matchFront && matchBack && itemName.startsWith(checkName)) return true;
            else if (itemName.equals(checkName)) return true;
        }

        return false;
    }

    @Override
    public boolean setField(String fieldName, String fieldValue) {
        oreName = fieldValue;
        if (oreName.startsWith("*")) matchFront = true;
        else matchFront = false;
        if (oreName.endsWith("*")) matchBack = true;
        else matchBack = false;
        matchBoth = matchFront && matchBack;
        checkName = oreName.replaceAll("\\*", "");
        return true;
    }
}
