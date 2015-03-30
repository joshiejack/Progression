package joshie.crafting.trigger;

import joshie.crafting.api.Bus;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ITrigger;
import joshie.crafting.api.ITriggerData;
import joshie.crafting.gui.IItemSelectable;
import joshie.crafting.gui.SelectItemOverlay;
import joshie.crafting.gui.SelectItemOverlay.Type;
import joshie.crafting.gui.SelectTextEdit;
import joshie.crafting.gui.SelectTextEdit.ITextEditable;
import joshie.crafting.helpers.StackHelper;
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
        super("Crafting", 0xFF663300, "crafting");
        editCraftAmount = new CraftAmount(this);
        editItemAmount = new ItemAmount(this);
    }

    @Override
    public ITrigger newInstance() {
        return new TriggerCrafting();
    }

    @Override
    public Bus getBusType() {
        return Bus.FML;
    }

    @SubscribeEvent
    public void onEvent(ItemCraftedEvent event) {
        CraftingAPI.registry.fireTrigger(event.player, getTypeName(), event.crafting.copy());
    }

    @Override
    public ITrigger deserialize(JsonObject data) {
        TriggerCrafting trigger = new TriggerCrafting();
        trigger.stack = StackHelper.getStackFromString(data.get("item").getAsString());
        if (data.get("matchDamage") != null) {
            trigger.matchDamage = data.get("matchDamage").getAsBoolean();
        }

        if (data.get("matchNBT") != null) {
            trigger.matchNBT = data.get("matchNBT").getAsBoolean();
        }

        if (data.get("craftingTimes") != null) {
            trigger.craftingTimes = data.get("craftingTimes").getAsInt();
        }

        if (data.get("itemAmount") != null) {
            trigger.itemAmount = data.get("itemAmount").getAsInt();
        }

        return trigger;
    }

    @Override
    public void serialize(JsonObject data) {
        data.addProperty("item", StackHelper.getStringFromStack(stack));
        if (matchDamage != true) data.addProperty("matchDamage", matchDamage);
        if (matchNBT != false) data.addProperty("matchNBT", matchNBT);
    }

    @Override
    public ITriggerData newData() {
        return new DataCrafting();
    }

    @Override
    public boolean isCompleted(ITriggerData existing) {
        DataCrafting data = (DataCrafting) existing;
        return data.amountCrafted >= itemAmount && data.timesCrafted >= craftingTimes;
    }

    @Override
    public void onFired(ITriggerData existing, Object... additional) {
        DataCrafting data = (DataCrafting) existing;
        ItemStack crafted = asStack(additional);
        if (stack.getItem() == crafted.getItem()) {
            if (matchDamage && stack.getItemDamage() != crafted.getItemDamage()) return;
            if (matchNBT && stack.getTagCompound() != crafted.getTagCompound()) return;
            data.amountCrafted += stack.stackSize;
            data.timesCrafted++;
        }
    }

    @Override
    public Result clicked() {
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
    public void draw() {
        drawStack(stack, 76, 44, 1.4F);
        int typeColor = 0xFF000000;
        int matchColor = 0xFF000000;
        int match2Color = 0xFF000000;
        int usageColor = 0xFF000000;
        if (mouseX <= 84 && mouseX >= 1) {
            if (mouseY >= 17 && mouseY <= 25) typeColor = 0xFFBBBBBB;
            if (mouseY > 25 && mouseY <= 33) matchColor = 0xFFBBBBBB;
            if (mouseY > 34 && mouseY <= 41) match2Color = 0xFFBBBBBB;
            if (mouseY > 42 && mouseY <= 50) usageColor = 0xFFBBBBBB;
        }

        drawText("matchDamage: " + matchDamage, 4, 18, typeColor);
        drawText("matchNBT: " + matchNBT, 4, 26, matchColor);
        drawText("craftingTimes: " + craftingTimes, 4, 34, match2Color);
        drawText("itemAmount: " + itemAmount, 4, 42, usageColor);
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
