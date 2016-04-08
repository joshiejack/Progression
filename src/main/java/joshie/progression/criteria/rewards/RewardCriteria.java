package joshie.progression.criteria.rewards;

import joshie.progression.Progression;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.ICustomDescription;
import joshie.progression.api.special.ICustomTooltip;
import joshie.progression.api.special.IGetterCallback;
import joshie.progression.api.special.IInit;
import joshie.progression.handlers.APIHandler;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

@ProgressionRule(name="criteria", color=0xFF99B3FF, meta="clearOrReceiveOrBlockCriteria")
public class RewardCriteria extends RewardBaseSingular implements IInit, ICustomDescription, ICustomTooltip, IGetterCallback {
    private ICriteria criteria = null;
    public boolean remove = true;
    public boolean possibility = false;
    public String displayName = "";

    @Override
    public void init(boolean isClient) {
        try {
            for (ICriteria c : APIHandler.getCache(isClient).getCriteria().values()) {
                if (c.getLocalisedName().equals(displayName)) {
                    criteria = c;
                    break;
                }
            }
        } catch (Exception e) {}
    }

    @Override
    public String getDescription() {
        if (criteria != null) {
            StringBuilder builder = new StringBuilder();
            if (remove) builder.append(Progression.format(getProvider().getUnlocalisedName() + ".remove.description", criteria.getLocalisedName()));
            else builder.append(Progression.format(getProvider().getUnlocalisedName() + ".add.description", criteria.getLocalisedName()));
            if (possibility) {
                builder.append("\n");
                builder.append(Progression.format(getProvider().getUnlocalisedName() + ".possibility.description", criteria.getLocalisedName()));
            }

            return builder.toString();
        }

        return Progression.translate(getProvider().getUnlocalisedName() + ".incorrect");
    }

    @Override
    public void addTooltip(List list) {
        if (criteria != null) {
            if (remove) {
                list.add(Progression.translate("remove") + " " + criteria.getLocalisedName());
            } else list.add(Progression.translate("add") + " " + criteria.getLocalisedName());
        }
    }

    @Override
    public String getField(String fieldName) {
        return criteria != null ? EnumChatFormatting.GREEN + displayName : EnumChatFormatting.RED + displayName;
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
}
