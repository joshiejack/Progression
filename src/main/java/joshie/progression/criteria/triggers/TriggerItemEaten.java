package joshie.progression.criteria.triggers;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.gui.SelectItemOverlay.Type;
import joshie.progression.gui.fields.BooleanField;
import joshie.progression.gui.fields.ItemField;
import joshie.progression.helpers.JSONHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class TriggerItemEaten extends TriggerBaseCounter {
    public ItemStack stack = new ItemStack(Items.apple);
    public boolean matchDamage = true;
    public boolean matchNBT = false;

    public TriggerItemEaten() {
        super("onEaten", 0xFF00B285);
        list.add(new BooleanField("matchDamage", this));
        list.add(new BooleanField("matchNBT", this));
        list.add(new ItemField("stack", this, 76, 44, 1.4F, 77, 100, 43, 68, Type.TRIGGER));
    }

    @SubscribeEvent
    public void onEvent(PlayerUseItemEvent.Finish event) {
        ProgressionAPI.registry.fireTrigger(event.entityPlayer, getUnlocalisedName(), event.item);
    }

    @Override
    public void readFromJSON(JsonObject data) {
        super.readFromJSON(data);
        stack = JSONHelper.getItemStack(data, "item", new ItemStack(Blocks.crafting_table));
        matchDamage = JSONHelper.getBoolean(data, "matchDamage", matchDamage);
        matchNBT = JSONHelper.getBoolean(data, "matchNBT", matchNBT);
    }

    @Override
    public void writeToJSON(JsonObject data) {
        super.writeToJSON(data);
        JSONHelper.setItemStack(data, "item", stack);
        JSONHelper.setBoolean(data, "matchDamage", matchDamage, true);
        JSONHelper.setBoolean(data, "matchNBT", matchNBT, false);
    }

    @Override
    protected boolean canIncrease(Object... data) {
        ItemStack item = (ItemStack) data[0];
        if (item.getItem() != stack.getItem()) return false;
        if (matchDamage && item.getItemDamage() != stack.getItemDamage()) return false;
        if (matchNBT && item.getTagCompound() != stack.getTagCompound()) return false;
        return true;
    }
}
