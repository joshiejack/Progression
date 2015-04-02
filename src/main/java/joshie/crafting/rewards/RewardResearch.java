package joshie.crafting.rewards;

import java.util.List;
import java.util.UUID;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.IReward;
import joshie.crafting.gui.SelectTextEdit;
import joshie.crafting.gui.SelectTextEdit.ITextEditable;
import joshie.crafting.helpers.ClientHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.Event.Result;

public class RewardResearch extends RewardBase implements ITextEditable {
    private String research = "dummy";

    public RewardResearch() {
        super("Give Research", 0xFF99B3FF, "research");
    }

    @Override
    public IReward newInstance() {
        return new RewardResearch();
    }

    @Override
    public IReward deserialize(JsonObject data) {
        RewardResearch reward = new RewardResearch();
        reward.research = data.get("researchName").getAsString();
        return reward;
    }

    @Override
    public void serialize(JsonObject elements) {
        elements.addProperty("researchName", research);
    }

    @Override
    public void reward(UUID uuid) {
        CraftingAPI.players.getServerPlayer(uuid).getMappings().fireAllTriggers("research", research);
    }

    //TODO: Replace this with a research icon
    @Override
    public ItemStack getIcon() {
        return new ItemStack(Items.potionitem);
    }

    @Override
    public Result clicked() {
        if (mouseX <= 84 && mouseX >= 1) {
            if (mouseY >= 17 && mouseY <= 33) {
                SelectTextEdit.INSTANCE.select(this);
                return Result.ALLOW;
            }
        }

        return Result.DEFAULT;
    }

    @Override
    public void draw() {
        int researchColor = 0xFFFFFFFF;
        if (ClientHelper.canEdit()) {
            if (mouseX <= 84 && mouseX >= 1) {
                if (mouseY >= 17 && mouseY <= 33) researchColor = 0xFFBBBBBB;
            }
        }

        if (SelectTextEdit.INSTANCE.getEditable() == this) {
            drawText("research: ", 4, 18, researchColor);
            drawText(SelectTextEdit.INSTANCE.getText(), 4, 26, researchColor);
        } else {
            drawText("research: ", 4, 18, researchColor);
            drawText(getTextField(), 4, 26, researchColor);
        }
    }

    @Override
    public String getTextField() {
        return research;
    }

    @Override
    public void setTextField(String text) {
        this.research = text;
    }

    @Override
    public void addTooltip(List list) {
        list.add(EnumChatFormatting.WHITE + "Free Research:");
        list.add(research);
    }
}
