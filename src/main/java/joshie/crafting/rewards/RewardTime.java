package joshie.crafting.rewards;

import java.util.List;
import java.util.UUID;

import joshie.crafting.gui.TextList.BooleanField;
import joshie.crafting.gui.TextList.TextField;
import joshie.crafting.helpers.JSONHelper;
import joshie.crafting.helpers.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.google.gson.JsonObject;

public class RewardTime extends RewardBase {
    public boolean type = false;
    public int time = 0;

    public RewardTime() {
        super(new ItemStack(Items.clock), "time", 0xFF26C9FF);
        list.add(new BooleanField("type", this, "add", "set"));
        list.add(new TextField("time", this));
    }

    @Override
    public void readFromJSON(JsonObject data) {
        type = JSONHelper.getBoolean(data, "add", type);
        time = JSONHelper.getInteger(data, "time", time);
    }

    @Override
    public void writeToJSON(JsonObject data) {
        JSONHelper.setBoolean(data, "add", type, false);
        JSONHelper.setInteger(data, "time", time, 0);
    }

    @Override
    public void reward(UUID uuid) {
        EntityPlayer player = PlayerHelper.getPlayerFromUUID(uuid);
        if (player != null) {
            if (type) {
                player.worldObj.setWorldTime(player.worldObj.getWorldTime() + (long) time);
            } else player.worldObj.setWorldTime(time);
        }
    }

    @Override
    public void addTooltip(List list) {
        if (type) list.add("Add " + time + " ticks to time");
        else list.add("Set time to " + time);
    }
}
