package joshie.progression.criteria.filters;

import java.util.Set;

import com.google.common.collect.HashMultimap;

import joshie.progression.api.IInitAfterRead;
import joshie.progression.api.ISetterCallback;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class FilterOre extends FilterBase implements ISetterCallback, IInitAfterRead {
    private static HashMultimap<String, String> cache = HashMultimap.create();
    private String checkName;
    private boolean matchBoth;
    private boolean matchFront;
    private boolean matchBack;
    
    public String oreName = "ingotIron";

    public FilterOre() {
        super("oreDictionary", 0xFFB25900);
    }

    @Override
    public void init() {
        setField("oreName", oreName);
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
                cache.get(key).add(OreDictionary.getOreName(i));
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
    public boolean setField(String fieldName, Object object) {
        String fieldValue = (String) object;
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
