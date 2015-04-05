package joshie.crafting.rewards;

import java.util.List;
import java.util.UUID;

import joshie.crafting.CraftAPIRegistry;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICriteria;
import joshie.crafting.api.IReward;
import joshie.crafting.gui.SelectTextEdit;
import joshie.crafting.gui.SelectTextEdit.ITextEditable;
import joshie.crafting.helpers.ClientHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.Event.Result;

public class RewardCriteria extends RewardBase implements ITextEditable {
    private String criteriaID = "";
    private boolean remove = false;
    private ICriteria criteria = null;

    public RewardCriteria() {
        super("Give Criteria", 0xFF99B3FF, "criteria");
    }

    @Override
    public IReward newInstance() {
        return new RewardCriteria();
    }

    @Override
    public IReward deserialize(JsonObject data) {
        RewardCriteria reward = new RewardCriteria();
        reward.criteriaID = data.get("criteriaID").getAsString();
        if (data.get("remove") != null) {
            reward.remove = data.get("remove").getAsBoolean();
        }

        return reward;
    }

    @Override
    public void serialize(JsonObject elements) {
        elements.addProperty("criteriaID", criteriaID);
        if (remove != false) elements.addProperty("remove", remove);
    }

    public ICriteria getCriteria() {
        return CraftingAPI.registry.getCriteriaFromName(criteriaID);
    }

    @Override
    public void reward(UUID uuid) {
        criteria = getCriteria();
        if (criteria == null) return; //Do not give the reward
        if (remove) {
            CraftingAPI.players.getServerPlayer(uuid).getMappings().fireAllTriggers("forced-remove", criteria);
        } else CraftingAPI.players.getServerPlayer(uuid).getMappings().fireAllTriggers("forced-complete", criteria);
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Items.golden_apple);
    }

    @Override
    public Result clicked() {
        if (mouseX <= 84 && mouseX >= 1) {
            if (mouseY >= 17 && mouseY <= 33) {
                SelectTextEdit.INSTANCE.select(this);
                return Result.ALLOW;
            } else if (mouseY > 33 && mouseY <= 41) {
                remove = !remove;
                return Result.ALLOW;
            }
        }

        return Result.DEFAULT;
    }

    @Override
    public void draw() {
        int researchColor = 0xFFFFFFFF;
        int booleanColor = 0xFFFFFFFF;
        if (ClientHelper.canEdit()) {
            if (mouseX <= 84 && mouseX >= 1) {
                if (mouseY >= 17 && mouseY <= 33) researchColor = 0xFFBBBBBB;
                if (mouseY > 33 && mouseY <= 41) booleanColor = 0xFFBBBBBB;
            }
        }

        drawText("criteria: ", 4, 18, researchColor);
        EnumChatFormatting prefix = criteria != null? EnumChatFormatting.GREEN: EnumChatFormatting.RED;
        drawText(prefix + SelectTextEdit.INSTANCE.getText(this), 4, 26, researchColor);
        drawText("remove: " + remove, 4, 33, booleanColor);
    }

    private String displayName = null;

    @Override
    public String getTextField() {
        if (displayName == null) {
            criteria = CraftingAPI.registry.getCriteriaFromName(criteriaID);
            if (criteria == null) {
                displayName = "null";
            } else displayName = criteria.getDisplayName();
        }

        return criteria != null ? displayName : displayName;
    }

    @Override
    public void setTextField(String text) {
        displayName = text;

        try {
            criteria = null;
            for (ICriteria c : CraftAPIRegistry.criteria.values()) {
                String display = c.getDisplayName();
                if (c.getDisplayName().equals(displayName)) {
                    criteria = c;
                    criteriaID = c.getUniqueName();
                    break;
                }
            }
        } catch (Exception e) {}
    }

    @Override
    public void addTooltip(List list) {
        if (criteria == null) {
            criteria = getCriteria();
        }

        if (criteria != null) {
            if (remove) {
                list.add("Remove " + criteria.getDisplayName());
            } else list.add("Add " + criteria.getDisplayName());
        }
    }
}
