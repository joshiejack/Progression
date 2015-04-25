package joshie.crafting.rewards;

import java.util.List;
import java.util.UUID;

import joshie.crafting.gui.TextFieldHelper;
import joshie.crafting.gui.TextList.SplitTextSelector;
import joshie.crafting.helpers.JSONHelper;
import joshie.crafting.player.PlayerTracker;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.google.gson.JsonObject;

public class RewardResearch extends RewardBase {
    public String research = "dummy";

    public RewardResearch() {
        super(new ItemStack(Items.potionitem), "research", 0xFF99B3FF);
        list.add(new SplitTextSelector("name", new TextFieldHelper("research", this)));
    }

    @Override
    public void readFromJSON(JsonObject data) {
        research = JSONHelper.getString(data, "researchName", research);
    }

    @Override
    public void writeToJSON(JsonObject data) {
        JSONHelper.setString(data, "researchName", research, "dummy");
    }

    @Override
    public void reward(UUID uuid) {
        PlayerTracker.getServerPlayer(uuid).getMappings().fireAllTriggers("research", research);
    }

    @Override
    public void addTooltip(List list) {
        list.add(EnumChatFormatting.WHITE + "Free Research:");
        list.add(research);
    }
}
