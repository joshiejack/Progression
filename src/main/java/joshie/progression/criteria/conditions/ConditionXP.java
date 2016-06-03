package joshie.progression.criteria.conditions;

import joshie.progression.Progression;
import joshie.progression.api.IPlayerTeam;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.ICustomDescription;
import net.minecraft.entity.player.EntityPlayer;

@ProgressionRule(name="ifHasXP", color=0xFF589653, icon="minecraft:experience_bottle")
public class ConditionXP extends ConditionBase implements ICustomDescription {
    public boolean checkLevel;
    public int amount = 1;
    public boolean greaterThan = true;
    public boolean isEqualTo = true;
    public boolean lesserThan = false;

    @Override
    public String getDescription() {
        String suffix = getProvider().isInverted() ? ".inverted" : "";
        if (greaterThan && isEqualTo) return Progression.format(getProvider().getUnlocalisedName() + ".greater.equal" + suffix, amount);
        else if (lesserThan && isEqualTo) return Progression.format(getProvider().getUnlocalisedName() + ".lesser.equal" + suffix, amount);
        else if (greaterThan) return Progression.format(getProvider().getUnlocalisedName() + ".greater" + suffix, amount);
        else if (lesserThan) return Progression.format(getProvider().getUnlocalisedName() + ".lesser" + suffix, amount);
        else if (isEqualTo) return Progression.format(getProvider().getUnlocalisedName() + ".equal" + suffix, amount);
        else return "INVALID SETUP";
    }

    @Override
    public boolean isSatisfied(IPlayerTeam team) {
        int counter = 0;
        for (EntityPlayer player: team.getTeamEntities()) {
            if (!team.isTrueTeam()) counter = 0; //Reset the counter
            if (!checkLevel) counter += player.experienceTotal;
            else counter += player.experienceLevel;
            if (isValidValue(counter)) return true;
        }

        return false;
    }

    //Helper Methods
    private boolean isValidValue(double total) {
        if (greaterThan && total > amount) return true;
        if (isEqualTo && total == amount) return true;
        if (lesserThan && total < amount) return true;

        //FALSE BABY!!!
        return false;
    }
}