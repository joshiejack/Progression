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
        super("obtain", 0xFFFFFF00, "crafting");
    }

    @Override
    public Bus[] getEventBusTypes() {
        return new Bus[] { Bus.FML, Bus.FORGE };
    }

    @SubscribeEvent
    public void onEvent(PlayerOpenContainerEvent event) {
        for (int i = 0; i < event.entityPlayer.inventory.mainInventory.length; i++) {
            ItemStack stack = event.entityPlayer.inventory.mainInventory[i];
            if (stack == null && event.canInteractWith) continue;
            CraftingAPI.registry.fireTrigger(event.entityPlayer, getUnlocalisedName(), stack, event.entityPlayer, i);
        }
    }
    
    @Override
    public void readFromJSON(JsonObject data) {
        stack = JSONHelper.getItemStack(data, "item", new ItemStack(Blocks.crafting_table));
        matchDamage = JSONHelper.getBoolean(data, "matchDamage", matchDamage);
        matchNBT = JSONHelper.getBoolean(data, "matchNBT", matchNBT);
        itemAmount = JSONHelper.getInteger(data, "itemAmount", itemAmount);
        consume = JSONHelper.getBoolean(data, "consume", consume);
    }

    @Override
    public void writeToJSON(JsonObject data) {
        JSONHelper.setItemStack(data, "item", stack);
        JSONHelper.setBoolean(data, "matchDamage", matchDamage, true);
        JSONHelper.setBoolean(data, "matchNBT", matchNBT, false);
        JSONHelper.setInteger(data, "itemAmount", itemAmount, 1);
        JSONHelper.setBoolean(data, "consume", consume, false);
    }

    @Override
    public boolean isCompleted(ITriggerData existing) {
        DataCrafting data = (DataCrafting) existing;
        return data.amountCrafted >= itemAmount;
    }

    @Override
    public void onFired(UUID uuid, ITriggerData existing, Object... additional) {
        DataCrafting data = (DataCrafting) existing;
        ItemStack crafted = (ItemStack)additional[0];
        if (stack.getItem() == crafted.getItem()) {
            if (matchDamage && stack.getItemDamage() != crafted.getItemDamage()) return;
            if (matchNBT && stack.getTagCompound() != crafted.getTagCompound()) return;
            data.amountCrafted += crafted.stackSize;
            data.timesCrafted++;

            if (consume) {
                EntityPlayer player = (EntityPlayer) additional[1];
                int slot = (Integer) additional[2];
                player.inventory.decrStackSize(slot, stack.stackSize);
            }
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
            if (mouseY > 34 && mouseY <= 41) SelectTextEdit.INSTANCE.select(this);
            if (mouseY > 42 && mouseY <= 50) consume = !consume;
            if (mouseY >= 17 && mouseY <= 50) return Result.ALLOW;
        }

        return Result.DEFAULT;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        DrawHelper.triggerDraw.drawStack(stack, 76, 44, 1.4F);
        int typeColor = Theme.INSTANCE.optionsFontColor;
        int matchColor = Theme.INSTANCE.optionsFontColor;
        int match2Color = Theme.INSTANCE.optionsFontColor;
        int consumeColor = Theme.INSTANCE.optionsFontColor;
        if (ClientHelper.canEdit()) {
            if (mouseX <= 84 && mouseX >= 1) {
                if (mouseY >= 17 && mouseY <= 25) typeColor = Theme.INSTANCE.optionsFontColorHover;
                if (mouseY > 25 && mouseY <= 33) matchColor = Theme.INSTANCE.optionsFontColorHover;
                if (mouseY > 34 && mouseY <= 41) match2Color = Theme.INSTANCE.optionsFontColorHover;
                if (mouseY > 42 && mouseY <= 50) consumeColor = Theme.INSTANCE.optionsFontColorHover;
            }
        }

        DrawHelper.triggerDraw.drawText("matchDamage: " + matchDamage, 4, 18, typeColor);
        DrawHelper.triggerDraw.drawText("matchNBT: " + matchNBT, 4, 26, matchColor);
        DrawHelper.triggerDraw.drawText("itemAmount: " + SelectTextEdit.INSTANCE.getText(this), 4, 34, match2Color);
        DrawHelper.triggerDraw.drawText("consume: " + consume, 4, 42, consumeColor);
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
}
