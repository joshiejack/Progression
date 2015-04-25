package joshie.crafting.rewards;

import java.util.List;
import java.util.UUID;

import joshie.crafting.api.DrawHelper;
import joshie.crafting.gui.TextFieldHelper.IntegerFieldHelper;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.JSONHelper;
import joshie.crafting.helpers.PlayerHelper;
import joshie.crafting.json.Theme;
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
        super("time", 0xFF26C9FF);
        timeEdit = new IntegerFieldHelper("time", this);
    }

    @Override
    public void readFromJSON(JsonObject data) {
        add = JSONHelper.getBoolean(data, "add", add);
        time = JSONHelper.getInteger(data, "time", time);
    }

    @Override
    public void writeToJSON(JsonObject data) {
        JSONHelper.setBoolean(data, "add", add, false);
        JSONHelper.setInteger(data, "time", time, 0);
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
    public Result onClicked(int mouseX, int mouseY) {
        if (mouseX <= 84 && mouseX >= 1) {
            if (mouseY >= 17 && mouseY <= 25) add = !add;
            if (mouseY > 25 && mouseY <= 33) timeEdit.select();
            if (mouseY >= 17 && mouseY <= 33) return Result.ALLOW;
        }

        return Result.DEFAULT;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        int color = Theme.INSTANCE.optionsFontColor;
        int amountColor = Theme.INSTANCE.optionsFontColor;
        if (ClientHelper.canEdit()) {
            if (mouseX <= 84 && mouseX >= 1) {
                if (mouseY >= 17 && mouseY <= 25) color = Theme.INSTANCE.optionsFontColorHover;
                if (mouseY > 25 && mouseY <= 33) amountColor = Theme.INSTANCE.optionsFontColorHover;
            }
        }

        DrawHelper.drawText("type: " + (add ? "add" : "set"), 4, 18, color);
        DrawHelper.drawText("time: " + timeEdit, 4, 26, amountColor);
    }

    @Override
    public void addTooltip(List list) {
        if (add) list.add("Add " + time + " ticks to time");
        else list.add("Set time to " + time);
    }
}
