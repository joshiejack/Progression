package joshie.crafting.rewards;

import java.util.List;
import java.util.UUID;

import joshie.crafting.api.IReward;
import joshie.crafting.gui.SelectTextEdit;
import joshie.crafting.gui.SelectTextEdit.ITextEditable;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.Event.Result;

public class RewardExperience extends RewardBase implements ITextEditable {
    private int amount = 1;

    public RewardExperience() {
        super("Give Experience", 0xFF00B200, "experience");
    }

    @Override
    public IReward newInstance() {
        return new RewardExperience();
    }

    @Override
    public IReward deserialize(JsonObject data) {
        RewardExperience reward = new RewardExperience();
        reward.amount = data.get("amount").getAsInt();
        return reward;
    }

    @Override
    public void serialize(JsonObject elements) {
        elements.addProperty("amount", amount);
    }

    @Override
    public void reward(UUID uuid) {
        EntityPlayer player = PlayerHelper.getPlayerFromUUID(uuid);
        if (player != null) {
            player.addExperience(amount);
        }
    }

    private static final ItemStack experience = new ItemStack(Items.experience_bottle);

    @Override
    public ItemStack getIcon() {
        return experience;
    }

    @Override
    public Result clicked() {
        if (mouseX <= 84 && mouseX >= 1) {
            if (mouseY >= 17 && mouseY <= 25) {
                SelectTextEdit.INSTANCE.select(this);
                return Result.ALLOW;
            }
        }

        return Result.DEFAULT;
    }

    @Override
    public void draw() {
        int color = 0xFFFFFFFF;
        if (ClientHelper.canEdit()) {
            if (mouseX <= 84 && mouseX >= 1) {
                if (mouseY >= 17 && mouseY <= 25) color = 0xFFBBBBBB;
            }
        }
        
        if (SelectTextEdit.INSTANCE.getEditable() == this) {
            drawText("amount: " + SelectTextEdit.INSTANCE.getText(), 4, 18, color);
        } else drawText("amount: " + getTextField(), 4, 18, color);
    }

    private String textField;

    @Override
    public String getTextField() {
        if (textField == null) {
            textField = "" + amount;
        }

        return textField;
    }

    @Override
    public void setTextField(String text) {
        String fixed = text.replaceAll("[^0-9]", "");
        this.textField = fixed;

        try {
            this.amount = Integer.parseInt(textField);
        } catch (Exception e) {
            this.amount = 1;
        }
    }

    @Override
    public void addTooltip(List list) {
        list.add("" + EnumChatFormatting.WHITE + amount + " Experience Points");
    }
}
