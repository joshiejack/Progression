package joshie.progression.criteria.conditions;

import joshie.progression.Progression;
import joshie.progression.api.IPlayerTeam;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.ICustomDescription;
import joshie.progression.api.special.IGetterCallback;
import joshie.progression.api.special.IInit;
import joshie.progression.handlers.APIHandler;
import joshie.progression.player.PlayerTracker;
import net.minecraft.util.EnumChatFormatting;

import java.util.UUID;

@ProgressionRule(name="criteria", meta="ifCriteriaCompleted")
public class ConditionHasCriteria extends ConditionBase implements IInit, ICustomDescription, IGetterCallback {
    private ICriteria criteria = null;
    private UUID criteriaID = UUID.randomUUID();
    public String displayName = "";

    @Override
    public void init(boolean isClient) {
        try {
            for (ICriteria c : APIHandler.getCache(isClient).getCriteria().values()) {
                String display = c.getLocalisedName();
                if (c.getLocalisedName().equals(displayName)) {
                    criteria = c;
                    criteriaID = c.getUniqueID();
                    break;
                }
            }
        } catch (Exception e) {}
    }

    @Override
    public String getDescription() {
        if (criteria != null) {
            if (getProvider().isInverted()) return Progression.format(getProvider().getUnlocalisedName() + ".description.inverted", criteria.getLocalisedName());
            else return Progression.format(getProvider().getUnlocalisedName() + ".description", criteria.getLocalisedName());
        } else return "BROKEN CRITERIA";
    }

    @Override
    public String getField(String fieldName) {
        return criteria != null ? EnumChatFormatting.GREEN + displayName : EnumChatFormatting.RED + displayName;
    }


    @Override
    public boolean isSatisfied(IPlayerTeam team) {
        if (criteria != null) {
            return PlayerTracker.getServerPlayer(team.getOwner()).getMappings().getCompletedCriteria().keySet().contains(criteria);
        }

        return false;
    }
}
