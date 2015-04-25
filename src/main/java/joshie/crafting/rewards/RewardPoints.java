package joshie.crafting.rewards;

import java.util.List;
import java.util.UUID;

import joshie.crafting.api.DrawHelper;
import joshie.crafting.gui.TextFieldHelper;
import joshie.crafting.gui.TextFieldHelper.IntegerFieldHelper;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.JSONHelper;
import joshie.crafting.json.Theme;
import joshie.crafting.player.PlayerTracker;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.Event.Result;

public class RewardPoints extends RewardBase {
    private TextFieldHelper nameEdit;
    private IntegerFieldHelper amountEdit;
    public String name = "research";
    public int amount = 1;

    public RewardPoints() {
        super("points", 0xFF002DB2);
        nameEdit = new TextFieldHelper("name", this);
        amountEdit = new IntegerFieldHelper("amount", this);
    }

    @Override
    public void readFromJSON(JsonObject data) {
        name = JSONHelper.getString(data, "name", name);
        amount = JSONHelper.getInteger(data, "amount", amount);
    }

    @Override
    public void writeToJSON(JsonObject elements) {
        JSONHelper.setString(elements, "name", name, "research");
        JSONHelper.setInteger(elements, "amount", amount, 1);
    }

    @Override
    public void reward(UUID uuid) {
        PlayerTracker.getServerPlayer(uuid).addPoints(name, amount);
    }

    //TODO: Replace this with a research icon
    @Override
    public ItemStack getIcon() {
        return new ItemStack(Items.potionitem);
    }

    @Override
    public Result onClicked(int mouseX, int mouseY) {
        if (mouseX <= 84 && mouseX >= 1) {
            if (mouseY >= 17 && mouseY <= 25) nameEdit.select();
            if (mouseY > 25 && mouseY <= 33) amountEdit.select();
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

        DrawHelper.drawText("name: " + nameEdit, 4, 18, color);
        DrawHelper.drawText("amount: " + amountEdit, 4, 26, amountColor);
    }

    @Override
    public void addTooltip(List list) {
        list.add("" + EnumChatFormatting.WHITE + amount + " " + name + " Points");
    }
}
