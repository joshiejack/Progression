package joshie.crafting.trigger;

import java.util.UUID;

import joshie.crafting.api.Bus;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.DrawHelper;
import joshie.crafting.api.ITriggerData;
import joshie.crafting.gui.IItemSelectable;
import joshie.crafting.gui.SelectItemOverlay;
import joshie.crafting.gui.SelectItemOverlay.Type;
import joshie.crafting.gui.SelectTextEdit;
import joshie.crafting.gui.SelectTextEdit.ITextEditable;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.JSONHelper;
import joshie.crafting.json.Theme;
import joshie.crafting.trigger.data.DataCrafting;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class TriggerCrafting extends TriggerBase implements IItemSelectable {
    private CraftAmount editCraftAmount;
    private ItemAmount editItemAmount;
    public ItemStack stack = new ItemStack(Blocks.crafting_table);
    public boolean matchDamage = true;
    public boolean matchNBT = false;
    public int craftingTimes = 1;
    public int itemAmount = 1;

    public TriggerCrafting() {
        super("crafting", 0xFF663300, "crafting");
        editCraftAmount = new CraftAmount(this);
        editItemAmount = new ItemAmount(this);
    }

    @Override
    public Bus getEventBus() {
        return Bus.FML;
    }

    @SubscribeEvent
    public void onEvent(ItemCraftedEvent event) {        
        CraftingAPI.registry.fireTrigger(event.player, getUnlocalisedName(), event.crafting.copy());
    }

    @Override
    public void readFromJSON(JsonObject data) {
        stack = JSONHelper.getItemStack(data, "item", new ItemStack(Blocks.crafting_table));
        matchDamage = JSONHelper.getBoolean(data, "matchDamage", matchDamage);
        matchNBT = JSONHelper.getBoolean(data, "matchNBT", matchNBT);
        craftingTimes = JSONHelper.getInteger(data, "craftingTimes", craftingTimes);
        itemAmount = JSONHelper.getInteger(data, "itemAmount", itemAmount);
    }

    @Override
    public void writeToJSON(JsonObject data) {
        JSONHelper.setItemStack(data, "item", stack);
        JSONHelper.setBoolean(data, "matchDamage", matchDamage, true);
        JSONHelper.setBoolean(data, "matchNBT", matchNBT, false);
        JSONHelper.setInteger(data, "craftingTimes", craftingTimes, 1);
        JSONHelper.setInteger(data, "itemAmount", itemAmount, 1);
    }

    @Override
    public boolean isCompleted(ITriggerData existing) {
        DataCrafting data = (DataCrafting) existing;
        return data.amountCrafted >= itemAmount && data.timesCrafted >= craftingTimes;
    }

    @Override
    public void onFired(UUID uuid, ITriggerData existing, Object... additional) {        
        DataCrafting data = (DataCrafting) existing;
        ItemStack crafted = (ItemStack)(additional[0]);        
        if (stack.getItem() == crafted.getItem()) {
            if (matchDamage && stack.getItemDamage() != crafted.getItemDamage()) return;
            if (matchNBT && stack.getTagCompound() != crafted.getTagCompound()) return;
            data.amountCrafted += crafted.stackSize;
            data.timesCrafted++;
        }
    }

    @Override
    public Result onClicked(int mouseX, int mouseY) {
        if (mouseX >= 77 && mouseX <= 100) {
            if (mouseY >= 43 && mouseY <= 68) {
                SelectItemOverlay.INSTANCE.select(this, Type.TRIGGER);
                return Result.ALLOW;
            }
        }

        if (mouseX <= 84 && mouseX >= 1) {
            if (mouseY >= 17 && mouseY <= 25) matchDamage = !matchDamage;
            if (mouseY > 25 && mouseY <= 33) matchNBT = !matchNBT;
            if (mouseY > 34 && mouseY <= 41) SelectTextEdit.INSTANCE.select(editCraftAmount);
            if (mouseY > 42 && mouseY <= 50) SelectTextEdit.INSTANCE.select(editItemAmount);
            if (mouseY >= 17 && mouseY <= 57) return Result.ALLOW;
        }

        return Result.DEFAULT;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        DrawHelper.triggerDraw.drawStack(stack, 76, 44, 1.4F);
        int typeColor = Theme.INSTANCE.optionsFontColor;
        int matchColor = Theme.INSTANCE.optionsFontColor;
        int match2Color = Theme.INSTANCE.optionsFontColor;
        int usageColor = Theme.INSTANCE.optionsFontColor;
        if (ClientHelper.canEdit()) {
            if (mouseX <= 84 && mouseX >= 1) {
                if (mouseY >= 17 && mouseY <= 25) typeColor = Theme.INSTANCE.optionsFontColorHover;
                if (mouseY > 25 && mouseY <= 33) matchColor = Theme.INSTANCE.optionsFontColorHover;
                if (mouseY > 34 && mouseY <= 41) match2Color = Theme.INSTANCE.optionsFontColorHover;
                if (mouseY > 42 && mouseY <= 50) usageColor = Theme.INSTANCE.optionsFontColorHover;
            }
        }

        DrawHelper.triggerDraw.drawText("matchDamage: " + matchDamage, 4, 18, typeColor);
        DrawHelper.triggerDraw.drawText("matchNBT: " + matchNBT, 4, 26, matchColor);
        DrawHelper.triggerDraw.drawText("craftingTimes: " + SelectTextEdit.INSTANCE.getText(editCraftAmount), 4, 34, match2Color);
        DrawHelper.triggerDraw.drawText("itemAmount: " + SelectTextEdit.INSTANCE.getText(editItemAmount), 4, 42, usageColor);
    }
    
    private class CraftAmount implements ITextEditable {
        private TriggerCrafting trigger;
        String textField;

        public CraftAmount(TriggerCrafting trigger) {
            this.trigger = trigger;
        }

        @Override
        public String getTextField() {
            if (textField == null) {
                textField = "" + trigger.craftingTimes;
            }

            return textField;
        }

        @Override
        public void setTextField(String text) {
            String fixed = text.replaceAll("[^0-9]", "");
            this.textField = fixed;

            try {
                trigger.craftingTimes = Integer.parseInt(textField);
            } catch (Exception e) {
                trigger.craftingTimes = 1;
            }
        }
    }

    private class ItemAmount implements ITextEditable {
        private TriggerCrafting trigger;
        String textField;

        public ItemAmount(TriggerCrafting trigger) {
            this.trigger = trigger;
        }

        @Override
        public String getTextField() {
            if (textField == null) {
                textField = "" + trigger.itemAmount;
            }

            return textField;
        }

        @Override
        public void setTextField(String text) {
            String fixed = text.replaceAll("[^0-9]", "");
            this.textField = fixed;

            try {
                trigger.itemAmount = Integer.parseInt(textField);
            } catch (Exception e) {
                trigger.itemAmount = 1;
            }
        }
    }

    @Override
    public void setItemStack(ItemStack stack) {
        this.stack = stack;
    }
}
