package joshie.crafting.rewards;

import java.util.UUID;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.IReward;
import joshie.crafting.gui.TextFieldHelper;
import joshie.crafting.gui.TextFieldHelper.IntegerFieldHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.Event.Result;

public class RewardPoints extends RewardBase {
    private TextFieldHelper nameEdit;
    private IntegerFieldHelper amountEdit;
    private String name = "research";
    private int amount = 1;

    public RewardPoints() {
        super("Points", 0xFF002DB2, "points");
        nameEdit = new TextFieldHelper("name", this);
        amountEdit = new IntegerFieldHelper("amount", this);
    }

    @Override
    public IReward newInstance() {
        return new RewardPoints();
    }

    @Override
    public IReward deserialize(JsonObject data) {
        RewardPoints reward = new RewardPoints();
        reward.name = data.get("name").getAsString();
        if (data.get("amount") != null) {
            reward.amount = Math.max(1, data.get("amount").getAsInt());
        }

        return reward;
    }

    @Override
    public void serialize(JsonObject elements) {
        elements.addProperty("name", name);
        if (amount > 1) {
            elements.addProperty("amount", amount);
        }
    }

    @Override
    public void reward(UUID uuid) {
        CraftingAPI.players.getServerPlayer(uuid).addPoints(name, amount);
    }

    //TODO: Replace this with a research icon
    @Override
    public ItemStack getIcon() {
        return new ItemStack(Items.potionitem);
    }

    @Override
    public Result clicked() {
        if (mouseX <= 84 && mouseX >= 1) {
            if (mouseY >= 17 && mouseY <= 25) nameEdit.select();
            if (mouseY > 25 && mouseY <= 33) amountEdit.select();
            if (mouseY >= 17 && mouseY <= 33) return Result.ALLOW;
        }

        return Result.DEFAULT;
    }

    @Override
    public void draw() {
        int color = 0xFF000000;
        int amountColor = 0xFF000000;
        if (mouseX <= 84 && mouseX >= 1) {
            if (mouseY >= 17 && mouseY <= 25) color = 0xFFBBBBBB;
            if (mouseY > 25 && mouseY <= 33) amountColor = 0xFFBBBBBB;
        }

        drawText("name: " + nameEdit, 4, 18, color);
        drawText("amount: " + amountEdit, 4, 26, amountColor);
    }
}
