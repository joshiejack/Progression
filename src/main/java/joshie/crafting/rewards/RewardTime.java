package joshie.crafting.rewards;

import java.util.List;
import java.util.UUID;

import joshie.crafting.api.IReward;
import joshie.crafting.gui.TextFieldHelper.IntegerFieldHelper;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.Event.Result;

public class RewardTime extends RewardBase {
    private IntegerFieldHelper timeEdit;
    private boolean add = false;
    public int time = 0;

    public RewardTime() {
        super("Adjust Time", 0xFF26C9FF, "time");
        timeEdit = new IntegerFieldHelper("time", this);
    }

    @Override
    public IReward newInstance() {
        return new RewardTime();
    }

    @Override
    public IReward deserialize(JsonObject data) {
        RewardTime reward = new RewardTime();
        if (data.get("add") != null) {
            reward.add = data.get("add").getAsBoolean();
        }

        if (data.get("time") != null) {
            reward.time = data.get("time").getAsInt();
        }

        return reward;
    }

    @Override
    public void serialize(JsonObject elements) {
        if (add != false) {
            elements.addProperty("add", add);
        }

        if (time != 0) {
            elements.addProperty("time", time);
        }
    }

    @Override
    public void reward(UUID uuid) {
        EntityPlayer player = PlayerHelper.getPlayerFromUUID(uuid);
        if (player != null) {
            if (add) {
                player.worldObj.setWorldTime(player.worldObj.getWorldTime() + (long) time);
            } else player.worldObj.setWorldTime(time);
        }
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Items.clock);
    }

    @Override
    public Result clicked() {
        if (mouseX <= 84 && mouseX >= 1) {
            if (mouseY >= 17 && mouseY <= 25) add = !add;
            if (mouseY > 25 && mouseY <= 33) timeEdit.select();
            if (mouseY >= 17 && mouseY <= 33) return Result.ALLOW;
        }

        return Result.DEFAULT;
    }

    @Override
    public void draw() {
        int color = 0xFFFFFFFF;
        int amountColor = 0xFFFFFFFF;
        if (ClientHelper.canEdit()) {
            if (mouseX <= 84 && mouseX >= 1) {
                if (mouseY >= 17 && mouseY <= 25) color = 0xFFBBBBBB;
                if (mouseY > 25 && mouseY <= 33) amountColor = 0xFFBBBBBB;
            }
        }

        drawText("type: " + (add ? "add" : "set"), 4, 18, color);
        drawText("time: " + timeEdit, 4, 26, amountColor);
    }

    @Override
    public void addTooltip(List list) {
        if (add) list.add("Add " + time + " ticks to time");
        else list.add("Set time to " + time);
    }
}
