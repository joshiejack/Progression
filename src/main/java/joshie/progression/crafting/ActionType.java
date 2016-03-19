package joshie.progression.crafting;

import java.util.Collection;
import java.util.HashMap;

import net.minecraft.util.StatCollector;

public class ActionType {
    public static final ActionType CRAFTING = new ActionType("CRAFTING");
    public static final ActionType FURNACE = new ActionType("FURNACE");
    public static final ActionType BREAKBLOCK = new ActionType("BREAKBLOCK");
    public static final ActionType HARVESTDROP = new ActionType("HARVESTDROP");
    public static final ActionType ENTITYDROP = new ActionType("ENTITYDROP");
    public static final ActionType ARCANE = new ActionType("ARCANE");
    private static HashMap<String, ActionType> registry;

    private final String name;

    public ActionType(String name) {
        this.name = name;
        if (registry == null) registry = new HashMap();
        registry.put(name, this);
    }

    public String getDisplayName() {
        return StatCollector.translateToLocal("progression.action." + name.toLowerCase());
    }

    public static ActionType getCraftingActionFromName(String name) {
        ActionType type = registry.get(name);
        return type != null ? type : ActionType.CRAFTING;
    }

    public static Collection<ActionType> values() {
        return registry.values();
    }
}
