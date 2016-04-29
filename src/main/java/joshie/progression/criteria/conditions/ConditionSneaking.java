package joshie.progression.criteria.conditions;

import joshie.progression.api.IPlayerTeam;
import joshie.progression.api.criteria.ProgressionRule;
import net.minecraft.entity.player.EntityPlayer;

@ProgressionRule(name="isSneaking", color=0xFFD96D00, meta="isSneaking")
public class ConditionSneaking extends ConditionBase {
    @Override
    public boolean isSatisfied(IPlayerTeam team) {
        for (EntityPlayer player: team.getTeamEntities()) {
            return player.isSneaking();
        }

        return false;
    }
}
