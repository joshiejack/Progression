package joshie.crafting.rewards;

import java.util.List;
import java.util.UUID;

import joshie.crafting.api.DrawHelper;
import joshie.crafting.gui.SelectTextEdit;
import joshie.crafting.gui.SelectTextEdit.ITextEditable;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.JSONHelper;
import joshie.crafting.json.Theme;
import joshie.crafting.player.PlayerTracker;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.Event.Result;

public class RewardResearch extends RewardBase implements ITextEditable {
    private String research = "dummy";

    public RewardResearch() {
        super("research", 0xFF99B3FF);
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

    //TODO: Replace this with a research icon
    @Override
    public ItemStack getIcon() {
        return new ItemStack(Items.potionitem);
    }

    @Override
    public Result onClicked(int mouseX, int mouseY) {
        if (mouseX <= 84 && mouseX >= 1) {
            if (mouseY >= 17 && mouseY <= 33) {
                SelectTextEdit.INSTANCE.select(this);
                return Result.ALLOW;
            }
        }

        return Result.DEFAULT;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        int researchColor = Theme.INSTANCE.optionsFontColor;
        if (ClientHelper.canEdit()) {
            if (mouseX <= 84 && mouseX >= 1) {
                if (mouseY >= 17 && mouseY <= 33) researchColor = Theme.INSTANCE.optionsFontColorHover;
            }
        }

        if (SelectTextEdit.INSTANCE.getEditable() == this) {
            DrawHelper.drawText("research: ", 4, 18, researchColor);
            DrawHelper.drawText(SelectTextEdit.INSTANCE.getText(), 4, 26, researchColor);
        } else {
            DrawHelper.drawText("research: ", 4, 18, researchColor);
            DrawHelper.drawText(getTextField(), 4, 26, researchColor);
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
