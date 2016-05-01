package joshie.progression.criteria.rewards;

import joshie.progression.Progression;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.*;
import joshie.progression.handlers.APICache;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StringUtils;

import java.util.List;

import static joshie.progression.api.special.DisplayMode.EDIT;

@ProgressionRule(name="criteria", color=0xFF99B3FF, meta="clearOrReceiveOrBlockCriteria")
public class RewardCriteria extends RewardBaseSingular implements IInit, ICustomDescription, ICustomTooltip, ICustomWidth, IGetterCallback {
    private ICriteria criteria = null;
    public boolean remove = true;
    public boolean possibility = false;
    public String displayName = "";
    public String description = "";
    public int displayWidth = 100;

    @Override
    public void init(boolean isClient) {
        try {
            for (ICriteria c : APICache.getCache(isClient).getCriteriaSet()) {
                if (c.getLocalisedName().equals(displayName)) {
                    criteria = c;
                    break;
                }
            }
        } catch (Exception e) {}
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == EDIT ? 100 : displayWidth;
    }

    @Override
    public String getDescription() {
        if (!StringUtils.isNullOrEmpty(description)) {
            return description;
        }

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
        if (fieldName.equals("displayName")) return criteria != null ? EnumChatFormatting.GREEN + displayName : EnumChatFormatting.RED + displayName;
        if (fieldName.equals("displayWidth")) return displayWidth + "";
        else return description;
    }

    @Override
    public void reward(EntityPlayerMP player) {
        if (criteria == null) return; //Do not give the reward
        if (remove) {
            PlayerTracker.getServerPlayer(player).getMappings().forceRemoval(criteria);
        } else PlayerTracker.getServerPlayer(player).getMappings().forceComplete(criteria);

        if (possibility) {
            PlayerTracker.getServerPlayer(player).getMappings().switchPossibility(criteria);
        }
    }
}
