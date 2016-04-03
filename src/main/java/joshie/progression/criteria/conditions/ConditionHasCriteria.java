package joshie.progression.criteria.conditions;

import joshie.progression.Progression;
import joshie.progression.api.IPlayerTeam;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.special.IGetterCallback;
import joshie.progression.api.special.IInit;
import joshie.progression.handlers.APIHandler;
import joshie.progression.items.ItemCriteria;
import joshie.progression.player.PlayerTracker;
import net.minecraft.util.EnumChatFormatting;

import java.util.UUID;

public class ConditionHasCriteria extends ConditionBase implements IGetterCallback, IInit {
    private ICriteria criteria = null;
    private UUID criteriaID = UUID.randomUUID();
    public String displayName = "";

    public ConditionHasCriteria() {
        super(ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.ifCriteriaCompleted), "criteria", 0xFF00FFBF);
    }

    @Override
    public void init() {
        try {
            for (ICriteria c : APIHandler.getCriteria().values()) {
                String display = c.getDisplayName();
                if (c.getDisplayName().equals(displayName)) {
                    criteria = c;
                    criteriaID = c.getUniqueID();
                    break;
                }
            }
        } catch (Exception e) {}
    }

    public ICriteria getAssignedCriteria() {
        return APIHandler.getCriteriaFromName(criteriaID);
    }

    @Override
    public boolean isSatisfied(IPlayerTeam team) {
        if (criteria == null) criteria = getAssignedCriteria();
        if (criteria != null) {
            return PlayerTracker.getServerPlayer(team.getOwner()).getMappings().getCompletedCriteria().keySet().contains(criteria);
        }

        return false;
    }

    @Override
    public String getField(String fieldName) {
        return criteria != null ? EnumChatFormatting.GREEN + displayName : EnumChatFormatting.RED + displayName;
    }

    @Override
    public String getConditionDescription() {
        if (criteria != null) {
            if (inverted) return Progression.format(getUnlocalisedName() + ".description.inverted", criteria.getDisplayName());
            else return Progression.format(getUnlocalisedName() + ".description", criteria.getDisplayName());
        } else return "BROKEN CRITERIA";
    }
}
