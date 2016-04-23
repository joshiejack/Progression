package joshie.progression.criteria.triggers;

import joshie.progression.Progression;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IField;
import joshie.progression.api.criteria.IFilterProvider;
import joshie.progression.api.criteria.ITrigger;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.IClickable;
import joshie.progression.api.special.IMiniIcon;
import joshie.progression.api.special.ISpecialFieldProvider;
import mezz.jei.GuiEventHandler;
import mezz.jei.JustEnoughItems;
import mezz.jei.ProxyCommonClient;
import mezz.jei.gui.Focus;
import mezz.jei.gui.ItemListOverlay;
import mezz.jei.gui.RecipesGui;
import mezz.jei.input.InputHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

import static joshie.progression.ItemProgression.ItemMeta.craft;
import static joshie.progression.ItemProgression.getStackFromMeta;

@ProgressionRule(name="crafting", color=0xFF663300)
public class TriggerCrafting extends TriggerBaseItemFilter implements IClickable, IMiniIcon, ISpecialFieldProvider {
    private static final ItemStack mini = getStackFromMeta(craft);

    public int timesCrafted = 1;
    protected transient int timesItemCrafted;

    @Override
    public ITrigger copy() {
        TriggerCrafting trigger = new TriggerCrafting();
        trigger.timesCrafted = timesCrafted;
        return copyCounter(copyFilter(trigger));
    }

    @Override
    public ItemStack getMiniIcon() {
        return mini;
    }

    @Override
    public String getDescription() {
        int percentageItemTotal = (counter * 100) / amount;
        int percentageCraftedTotal = (timesItemCrafted * 100) / timesCrafted;
        int percentageTotal = (percentageItemTotal + percentageCraftedTotal) / 2;
        return Progression.format("trigger.crafting.description", amount);
    }

    @Override
    public boolean onClicked(ItemStack stack) {
        try {
            //Fuck your privateness
            Field f = ProxyCommonClient.class.getDeclaredField("guiEventHandler");
            f.setAccessible(true);
            GuiEventHandler eventHandler = (GuiEventHandler) f.get((ProxyCommonClient)JustEnoughItems.getProxy());

            //DIE WITH FIRE
            f = GuiEventHandler.class.getDeclaredField("inputHandler");
            f.setAccessible(true);
            InputHandler inputHandler = (InputHandler) f.get(eventHandler);
            if (inputHandler == null) { //If it's not created, create it
                Field f2 = GuiEventHandler.class.getDeclaredField("itemListOverlay");
                f2.setAccessible(true);
                ItemListOverlay itemListOverlay = (ItemListOverlay) f2.get(eventHandler);
                RecipesGui recipesGui = new RecipesGui();
                inputHandler = new InputHandler(recipesGui, itemListOverlay);
                f.set(eventHandler, inputHandler);
            }

            //We're almost there
            f = InputHandler.class.getDeclaredField("recipesGui");
            f.setAccessible(true);
            RecipesGui recipesGui = (RecipesGui) f.get(inputHandler);
            recipesGui.showRecipes(new Focus(stack));
            return true;
        } catch (Exception e) { e.printStackTrace(); }


        return false;
    }

    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        if (mode == DisplayMode.EDIT) fields.add(ProgressionAPI.fields.getItemPreview(this, "filters", 30, 35, 1.9F));
        else fields.add(ProgressionAPI.fields.getItemPreview(this, "filters", 65, 35, 1.9F));
    }

    @SubscribeEvent
    public void onEvent(ItemCraftedEvent event) {
        ItemStack result = event.crafting.copy();
        if (result.stackSize != 0) {
            ProgressionAPI.registry.fireTrigger(event.player, getProvider().getUnlocalisedName(), event.crafting.copy());
        }
    }

    @Override
    public boolean isCompleted() {
        return counter >= amount && timesItemCrafted >= timesCrafted;
    }

    @Override
    public boolean onFired(UUID uuid, Object... additional) {
        ItemStack crafted = (ItemStack) (additional[0]);
        for (IFilterProvider filter : filters) {
            if (filter.getProvided().matches(crafted)) {
                counter += crafted.stackSize;
                timesItemCrafted++;
                return true;
            }
        }

        return true;
    }

    @Override
    public void readDataFromNBT(NBTTagCompound tag) {
        super.readDataFromNBT(tag);
        timesItemCrafted = tag.getInteger("Times");
    }

    @Override
    public void writeDataToNBT(NBTTagCompound tag) {
        super.writeDataToNBT(tag);
        tag.setInteger("Times", timesItemCrafted);
    }
}
