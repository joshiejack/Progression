package joshie.progression.criteria.rewards;

import java.util.List;
import java.util.UUID;

import com.google.gson.JsonObject;

import joshie.progression.gui.fields.TextField;
import joshie.progression.helpers.JSONHelper;
import joshie.progression.player.PlayerTracker;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class RewardPoints extends RewardBase {
    public String name = "research";
    public int amount = 1;

    public RewardPoints() {
        super(new ItemStack(Items.potionitem), "points", 0xFF002DB2);
        list.add(new TextField("name", this));
        list.add(new TextField("amount", this));
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

    @Override
    public void addTooltip(List list) {
        list.add("" + EnumChatFormatting.WHITE + amount + " " + name + " Points");
    }
}
