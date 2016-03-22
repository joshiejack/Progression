package joshie.progression.crafting;

import java.util.Collection;
import java.util.HashMap;

import joshie.progression.api.special.IHasEventBus;
import joshie.progression.crafting.actions.ActionBreakBlock;
import joshie.progression.crafting.actions.ActionHarvestDrop;
import joshie.progression.crafting.actions.ActionLivingDrop;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ActionType {
    public static final ActionType CRAFTING = new ActionType("CRAFTING").setItemStack(new ItemStack(Blocks.crafting_table));
    public static final ActionType FURNACE = new ActionType("FURNACE").setItemStack(new ItemStack(Blocks.furnace));
    public static final ActionType BREAKBLOCK = new ActionBreakBlock("BREAKBLOCK").setItemStack(new ItemStack(Items.iron_pickaxe));
    public static final ActionType HARVESTDROP = new ActionHarvestDrop("HARVESTDROP").setItemStack(new ItemStack(Items.redstone));
    public static final ActionType ENTITYDROP = new ActionLivingDrop("ENTITYDROP").setItemStack(new ItemStack(Items.rotten_flesh));
    public static final ActionType ARCANE = new ActionType("ARCANE").setItemStack(new ItemStack(Items.writable_book));
    private static HashMap<String, ActionType> registry;

    private final String name;
    private ItemStack stack;

    public ActionType(String name) {
        this.name = name;
        if (registry == null) registry = new HashMap();
        registry.put(name, this);
    }
    
    public ActionType setItemStack(ItemStack stack) {
        this.stack = stack;
        return this;
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

    public IHasEventBus getCustomBus() {
        return null;
    }

    public ItemStack getIcon() {
        return stack;
    }
}
