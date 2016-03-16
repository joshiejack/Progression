package joshie.progression.criteria.conditions;

import java.util.UUID;

import joshie.progression.api.ICriteria;
import joshie.progression.api.IGetterCallback;
import joshie.progression.api.ISetterCallback;
import joshie.progression.handlers.APIHandler;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class ConditionHasCriteria extends ConditionBase implements IGetterCallback, ISetterCallback {
    private ICriteria criteria = null;
    private String criteriaID = "";
    public String displayName = "";

    public ConditionHasCriteria() {
        super("criteria", 0xFF00FFBF);
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
        if (fieldName.equals(displayName)) {
            if (criteria == null) {
                criteria = APIHandler.getCriteriaFromName(criteriaID);
            }

            return criteria != null ? EnumChatFormatting.GREEN + displayName : EnumChatFormatting.RED + displayName;
        } else return null;
    }

    @Override
    public boolean setField(String fieldName, Object object) {
        String fieldValue = (String) object;
        if (fieldName.equals("displayName")) {
            displayName = fieldValue;

            try {
                for (ICriteria c : APIHandler.getCriteria().values()) {
                    String display = c.getDisplayName();
                    if (c.getDisplayName().equals(displayName)) {
                        criteria = c;
                        criteriaID = c.getUniqueName();
                        return true;
                    }
                }
            } catch (Exception e) {}
        }

        //FAILURE :D
        return false;
    }
}
