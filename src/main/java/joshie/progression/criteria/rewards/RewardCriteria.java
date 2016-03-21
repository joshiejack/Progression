package joshie.progression.criteria.rewards;

import java.util.List;
import java.util.UUID;

import joshie.progression.api.ICriteria;
import joshie.progression.api.IGetterCallback;
import joshie.progression.api.fields.IInit;
import joshie.progression.handlers.APIHandler;
import joshie.progression.player.PlayerTracker;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class RewardCriteria extends RewardBase implements IGetterCallback, IInit {
    private ICriteria criteria = null;
    private String criteriaID = "";
    public boolean remove = true;
    public boolean possibility = false;
    public String displayName = "";

    public RewardCriteria() {
        super(new ItemStack(Items.golden_apple), "criteria", 0xFF99B3FF);
    }

    @Override
    public void init() {
        try {
            for (ICriteria c : APIHandler.getCriteria().values()) {
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
    public void reward(UUID uuid) {
        if (criteria == null) return; //Do not give the reward
        if (remove) {
            PlayerTracker.getServerPlayer(uuid).getMappings().fireAllTriggers("forced-remove", criteria);
        } else PlayerTracker.getServerPlayer(uuid).getMappings().fireAllTriggers("forced-complete", criteria, criteria.getRewards());

        if (possibility) {
            PlayerTracker.getServerPlayer(uuid).getMappings().switchPossibility(criteria);
        }
    }

    @Override
    public String getField(String fieldName) {
        return criteria != null ? EnumChatFormatting.GREEN + displayName : EnumChatFormatting.RED + displayName;
    }

    @Override
    public void addTooltip(List list) {
        if (criteria != null) {
            if (remove) {
                list.add("Remove " + criteria.getDisplayName());
            } else list.add("Add " + criteria.getDisplayName());
        }
    }
}
