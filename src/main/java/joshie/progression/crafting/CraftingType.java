package joshie.progression.crafting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.StatCollector;

public class CraftingType {
    public static final List<CraftingType> craftingTypes = new ArrayList();
    public static final CraftingType CRAFTING = new CraftingType("crafting");
    public static final CraftingType FURNACE = new CraftingType("furnace");
    public static final CraftingType BREAKBLOCK = new CraftingType("breakblock");
    public static final CraftingType HARVEST = new CraftingType("harvestdrop");
    public static final CraftingType ENTITY = new CraftingType("entitydrop");
    
    public int id;
    public String name;
    
    public CraftingType(String name) {
        this.name = name;
        this.id = craftingTypes.size();
        craftingTypes.add(this);
    }

    public String getDisplayName() {
        return StatCollector.translateToLocal("progression.crafting.type." + name);
    }
}
