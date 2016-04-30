package joshie.progression.crafting;

import joshie.progression.api.criteria.IAction;
import joshie.progression.api.special.IHasEventBus;
import joshie.progression.crafting.actions.ActionBreakBlock;
import joshie.progression.crafting.actions.ActionGeneral;
import joshie.progression.crafting.actions.ActionHarvestDrop;
import joshie.progression.crafting.actions.ActionLivingDrop;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

import java.util.Collection;
import java.util.HashMap;

public class ActionType implements IAction {
    public static final ActionType CRAFTING = new ActionType("CRAFTING").setItemStack(new ItemStack(Blocks.CRAFTING_TABLE));
    public static final ActionType CRAFTINGUSE = new ActionType("CRAFTINGUSE").setItemStack(new ItemStack(Blocks.PLANKS));
    public static final ActionType FURNACE = new ActionType("FURNACE").setItemStack(new ItemStack(Items.COAL, 1, 1));
    public static final ActionType FURNACEUSE = new ActionType("FURNACEUSE").setItemStack(new ItemStack(Blocks.LOG));
    public static final ActionType GENERAL = new ActionType("GENERAL").setItemStack(new ItemStack(Blocks.BRICK_BLOCK)).setEventHandler(ActionGeneral.INSTANCE);
    public static final ActionType BREAKBLOCK = new ActionType("BREAKBLOCK").setItemStack(new ItemStack(Blocks.IRON_ORE)).setEventHandler(ActionBreakBlock.INSTANCE);
    public static final ActionType BREAKBLOCKWITH = new ActionType("BREAKBLOCKWITH").setItemStack(new ItemStack(Items.IRON_PICKAXE)).setEventHandler(ActionBreakBlock.INSTANCE);
    public static final ActionType HARVESTDROP = new ActionType("HARVESTDROP").setItemStack(new ItemStack(Items.REDSTONE)).setEventHandler(ActionHarvestDrop.INSTANCE);
    public static final ActionType HARVESTDROPWITH = new ActionType("HARVESTDROPWITH").setItemStack(new ItemStack(Items.IRON_AXE)).setEventHandler(ActionHarvestDrop.INSTANCE);
    public static final ActionType ENTITYDROP = new ActionType("ENTITYDROP").setItemStack(new ItemStack(Items.ROTTEN_FLESH)).setEventHandler(ActionLivingDrop.INSTANCE);
    public static final ActionType ENTITYDROPKILLEDWITH = new ActionType("ENTITYDROPKILLEDWITH").setItemStack(new ItemStack(Items.IRON_SWORD)).setEventHandler(ActionLivingDrop.INSTANCE);
    public static final ActionType ARCANE = new ActionType("ARCANE").setItemStack(new ItemStack(Items.WRITABLE_BOOK));
    public static final ActionType ARCANEUSE = new ActionType("ARCANEUSE").setItemStack(new ItemStack(Items.WRITTEN_BOOK));
    private static HashMap<String, ActionType> registry;
    private static HashMap<ItemStack, ActionType> itemRegistry;

    private IHasEventBus handler;
    private final String name;
    private ItemStack stack;

    public ActionType(String name) {
        this.name = name;
        if (registry == null) registry = new HashMap();
        registry.put(name, this);
    }

    @Override
    public ActionType setItemStack(ItemStack stack) {
        this.stack = stack;
        if (itemRegistry == null) itemRegistry = new HashMap();
        itemRegistry.put(stack, this);
        return this;
    }

    @Override
    public ActionType setEventHandler(IHasEventBus event) {
        handler = event;
        return this;
    }

    public String getDisplayName() {
        return I18n.translateToLocal("progression.action." + name.toLowerCase());
    }

    public String getUnlocalisedName() {
        return name;
    }

    public static ActionType getCraftingActionFromName(String name) {
        ActionType type = registry.get(name);
        return type != null ? type : ActionType.CRAFTING;
    }

    public static Collection<ActionType> values() {
        return registry.values();
    }

    public IHasEventBus getCustomBus() {
        return handler;
    }

    public ItemStack getIcon() {
        return stack;
    }

    public static ActionType getCraftingActionFromIcon(ItemStack stack) {
        ActionType type = itemRegistry.get(stack);
        if (type == null) {
            for (ActionType t: registry.values()) {
                if (t.getIcon().getItem() == stack.getItem() && t.getIcon().getItemDamage() == stack.getItemDamage()) {
                    itemRegistry.put(stack, t);
                    return t;
                }
            }

            return ActionType.CRAFTING;
        } else return type;
    }
}
