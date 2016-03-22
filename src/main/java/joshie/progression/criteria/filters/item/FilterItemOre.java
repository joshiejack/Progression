package joshie.progression.criteria.filters.item;

import java.util.Set;

import com.google.common.collect.HashMultimap;

import joshie.progression.api.special.IInit;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class FilterItemOre extends FilterBaseItem implements IInit {
    private static HashMultimap<String, String> cache = HashMultimap.create();
    private String checkName = "block";
    private boolean matchBoth;
    private boolean matchFront;
    private boolean matchBack;
    
    public String oreName = "block*";

    public FilterItemOre() {
        super("oreDictionary", 0xFFB25900);
    }
    
    public FilterItemOre(String string, int color) {
        super(string, color);
    }
   
    @Override
    public void init() {
        if (oreName.startsWith("*")) matchFront = true;
        else matchFront = false;
        if (oreName.endsWith("*")) matchBack = true;
        else matchBack = false;
        matchBoth = matchFront && matchBack;
        checkName = oreName.replaceAll("\\*", "");
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
}
