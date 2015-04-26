package joshie.progression.crafting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.StatCollector;

public class ActionType {
    public static final List<ActionType> craftingTypes = new ArrayList();
    public static final ActionType CRAFTING = new ActionType("crafting");
    public static final ActionType REPAIR = new ActionType("repair");
    public static final ActionType FURNACE = new ActionType("furnace");
    public static final ActionType BREAKBLOCK = new ActionType("breakblock");
    public static final ActionType HARVEST = new ActionType("harvestdrop");
    public static final ActionType ENTITY = new ActionType("entitydrop");
    
    public int id;
    public String name;
    
    public ActionType(String name) {
        this.name = name;
        this.id = craftingTypes.size();
        craftingTypes.add(this);
    }

    public String getDisplayName() {
        return StatCollector.translateToLocal("progression.action." + name);
    }
    
    public static ActionType getCraftingActionFromName(String name) {
        for (ActionType type : ActionType.craftingTypes) {
            if (name.equalsIgnoreCase(type.name)) return type;
        }

        return ActionType.CRAFTING;
    }
}
