package joshie.progression.criteria.rewards;

import joshie.progression.api.criteria.IProgressionCriteria;
import joshie.progression.api.special.IGetterCallback;
import joshie.progression.api.special.IInit;
import joshie.progression.handlers.APIHandler;
import joshie.progression.items.ItemCriteria;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;
import java.util.UUID;

public class RewardCriteria extends RewardBaseSingular implements IGetterCallback, IInit {
    private IProgressionCriteria criteria = null;
    private UUID criteriaID = UUID.randomUUID();
    public boolean remove = true;
    public boolean possibility = false;
    public String displayName = "";

    public RewardCriteria() {
        super(ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.clearOrReceiveOrBlockCriteria), "criteria", 0xFF99B3FF);
    }

    @Override
    public void init() {
        try {
            for (IProgressionCriteria c : APIHandler.getCriteria().values()) {
                String display = c.getDisplayName();
                if (c.getDisplayName().equals(displayName)) {
                    criteria = c;
                    criteriaID = c.getUniqueID();
                    break;
                }
            }
        } catch (Exception e) {}
    }

    @Override
    public void reward(EntityPlayerMP player) {
        if (criteria == null) return; //Do not give the reward
        if (remove) {
            PlayerTracker.getServerPlayer(player).getMappings().fireAllTriggers("forced-remove", criteria);
        } else PlayerTracker.getServerPlayer(player).getMappings().fireAllTriggers("forced-complete", criteria, criteria.getRewards());

        if (possibility) {
            PlayerTracker.getServerPlayer(player).getMappings().switchPossibility(criteria);
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
