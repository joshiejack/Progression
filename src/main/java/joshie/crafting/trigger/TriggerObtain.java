package joshie.crafting.trigger;

import java.util.UUID;

import joshie.crafting.api.Bus;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ITriggerData;
import joshie.crafting.gui.SelectItemOverlay.Type;
import joshie.crafting.gui.fields.BooleanField;
import joshie.crafting.gui.fields.ItemField;
import joshie.crafting.gui.fields.TextField;
import joshie.crafting.helpers.JSONHelper;
import joshie.crafting.trigger.data.DataCrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class TriggerObtain extends TriggerBase {
    public ItemStack stack = new ItemStack(Blocks.crafting_table);
    public boolean matchDamage = true;
    public boolean matchNBT = false;
    public int itemAmount = 1;
    public boolean consume = false;

    public TriggerObtain() {
        super("obtain", 0xFFFFFF00, "crafting");
        list.add(new BooleanField("matchDamage", this));
        list.add(new BooleanField("matchNBT", this));
        list.add(new TextField("itemAmount", this));
        list.add(new BooleanField("consume", this));
        list.add(new ItemField("stack", this, 76, 44, 1.4F, 77, 100, 43, 69, Type.TRIGGER));
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
}
