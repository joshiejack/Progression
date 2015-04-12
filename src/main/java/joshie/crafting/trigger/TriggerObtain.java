package joshie.crafting.trigger;

import java.util.List;

import joshie.crafting.api.Bus;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ITrigger;
import joshie.crafting.api.ITriggerData;
import joshie.crafting.gui.IItemSelectable;
import joshie.crafting.gui.SelectItemOverlay;
import joshie.crafting.gui.SelectItemOverlay.Type;
import joshie.crafting.gui.SelectTextEdit;
import joshie.crafting.gui.SelectTextEdit.ITextEditable;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.StackHelper;
import joshie.crafting.trigger.data.DataCrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class TriggerObtain extends TriggerBase implements IItemSelectable, ITextEditable {
    public ItemStack stack = new ItemStack(Blocks.crafting_table);
    public boolean matchDamage = true;
    public boolean matchNBT = false;
    public int itemAmount = 1;
    public boolean consume = false;

    public TriggerObtain() {
        super("Obtain", theme.triggerObtain, "obtain");
    }

    @Override
    public ITrigger newInstance() {
        return new TriggerObtain();
    }

    @Override
    public Bus[] getEventBuses() {
        return new Bus[] { Bus.FML, Bus.FORGE };
    }

    @SubscribeEvent
    public void onEvent(PlayerOpenContainerEvent event) {
        for (int i = 0; i < event.entityPlayer.inventory.mainInventory.length; i++) {
            ItemStack stack = event.entityPlayer.inventory.mainInventory[i];
            if (stack == null && event.canInteractWith) continue;
            CraftingAPI.registry.fireTrigger(event.entityPlayer, getTypeName(), stack, event.entityPlayer, i);
        }
    }

    @Override
    public ITrigger deserialize(JsonObject data) {
        TriggerObtain trigger = new TriggerObtain();
        trigger.stack = StackHelper.getStackFromString(data.get("item").getAsString());
        if (data.get("matchDamage") != null) {
            trigger.matchDamage = data.get("matchDamage").getAsBoolean();
        }

        if (data.get("matchNBT") != null) {
            trigger.matchNBT = data.get("matchNBT").getAsBoolean();
        }

        if (data.get("itemAmount") != null) {
            trigger.itemAmount = data.get("itemAmount").getAsInt();
        }

        if (data.get("consume") != null) {
            trigger.consume = data.get("consume").getAsBoolean();
        }

        return trigger;
    }

    @Override
    public void serialize(JsonObject data) {
        data.addProperty("item", StackHelper.getStringFromStack(stack));
        if (matchDamage != true) data.addProperty("matchDamage", matchDamage);
        if (matchNBT != false) data.addProperty("matchNBT", matchNBT);
        if (itemAmount != 1) data.addProperty("itemAmount", itemAmount);
        if (consume != false) data.addProperty("consume", consume);
    }

    @Override
    public ITriggerData newData() {
        return new DataCrafting();
    }

    @Override
    public boolean isCompleted(ITriggerData existing) {
        DataCrafting data = (DataCrafting) existing;
        return data.amountCrafted >= itemAmount;
    }

    @Override
    public void onFired(ITriggerData existing, Object... additional) {
        DataCrafting data = (DataCrafting) existing;
        ItemStack crafted = asStack(additional);
        if (stack.getItem() == crafted.getItem()) {
            if (matchDamage && stack.getItemDamage() != crafted.getItemDamage()) return;
            if (matchNBT && stack.getTagCompound() != crafted.getTagCompound()) return;
            data.amountCrafted += crafted.stackSize;
            data.timesCrafted++;

            if (consume) {
                EntityPlayer player = (EntityPlayer) additional[1];
                int slot = asInt(additional, 2);
                player.inventory.decrStackSize(slot, stack.stackSize);
            }
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
            if (mouseY > 34 && mouseY <= 41) SelectTextEdit.INSTANCE.select(this);
            if (mouseY > 42 && mouseY <= 50) consume = !consume;
            if (mouseY >= 17 && mouseY <= 50) return Result.ALLOW;
        }

        return Result.DEFAULT;
    }

    @Override
    public void draw() {
        drawStack(stack, 76, 44, 1.4F);
        int typeColor = theme.optionsFontColor;
        int matchColor = theme.optionsFontColor;
        int match2Color = theme.optionsFontColor;
        int consumeColor = theme.optionsFontColor;
        if (ClientHelper.canEdit()) {
            if (mouseX <= 84 && mouseX >= 1) {
                if (mouseY >= 17 && mouseY <= 25) typeColor = theme.optionsFontColorHover;
                if (mouseY > 25 && mouseY <= 33) matchColor = theme.optionsFontColorHover;
                if (mouseY > 34 && mouseY <= 41) match2Color = theme.optionsFontColorHover;
                if (mouseY > 42 && mouseY <= 50) consumeColor = theme.optionsFontColorHover;
            }
        }

        drawText("matchDamage: " + matchDamage, 4, 18, typeColor);
        drawText("matchNBT: " + matchNBT, 4, 26, matchColor);
        drawText("itemAmount: " + SelectTextEdit.INSTANCE.getText(this), 4, 34, match2Color);
        drawText("consume: " + consume, 4, 42, consumeColor);
    }

    private String textField;

    @Override
    public String getTextField() {
        if (textField == null) {
            textField = "" + itemAmount;
        }

        return textField;
    }

    @Override
    public void setTextField(String text) {
        String fixed = text.replaceAll("[^0-9]", "");
        this.textField = fixed;

        try {
            itemAmount = Integer.parseInt(textField);
        } catch (Exception e) {
            itemAmount = 1;
        }
    }

    @Override
    public void setItemStack(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public void addTooltip(List<String> toolTip) {
        toolTip.add("Obtain " + itemAmount + " " + stack.getDisplayName());
    }
}
