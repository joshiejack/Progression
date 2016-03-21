package joshie.progression.criteria.conditions;

import java.util.UUID;

import joshie.progression.api.ICriteria;
import joshie.progression.api.IGetterCallback;
import joshie.progression.api.fields.IInit;
import joshie.progression.handlers.APIHandler;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class ConditionHasCriteria extends ConditionBase implements IGetterCallback, IInit {
    private ICriteria criteria = null;
    private String criteriaID = "";
    public String displayName = "";

    public ConditionHasCriteria() {
        super("criteria", 0xFF00FFBF);
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

    public ICriteria getAssignedCriteria() {
        return APIHandler.getCriteriaFromName(criteriaID);
    }

    @Override
    public boolean isSatisfied(World world, EntityPlayer player, UUID uuid) {
        if (criteria == null) criteria = getAssignedCriteria();
        if (criteria != null) {
            return PlayerTracker.getServerPlayer(uuid).getMappings().getCompletedCriteria().keySet().contains(criteria);
        }

        return false;
    }

    @Override
    public String getField(String fieldName) {
        return criteria != null ? EnumChatFormatting.GREEN + displayName : EnumChatFormatting.RED + displayName;
    }
}
