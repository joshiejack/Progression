package joshie.progression.criteria.conditions;

import joshie.progression.Progression;
import joshie.progression.api.IPlayerTeam;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.ICustomDescription;

@ProgressionRule(name="points", color=0xFF59B200, meta="ifHasPoints")
public class ConditionPoints extends ConditionBase implements ICustomDescription {
    public String variable = "gold";
    public double amount = 1D;
    public boolean greaterThan = true;
    public boolean isEqualTo = true;
    public boolean lesserThan = false;

    @Override
    public String getDescription() {
        String suffix = getProvider().isInverted() ? ".inverted" : "";
        if (greaterThan && isEqualTo) return Progression.format(getProvider().getUnlocalisedName() + ".greater.equal" + suffix, amount, variable);
        else if (lesserThan && isEqualTo) return Progression.format(getProvider().getUnlocalisedName() + ".lesser.equal" + suffix, amount, variable);
        else if (greaterThan) return Progression.format(getProvider().getUnlocalisedName() + ".greater" + suffix, amount, variable);
        else if (lesserThan) return Progression.format(getProvider().getUnlocalisedName() + ".lesser" + suffix, amount, variable);
        else if (isEqualTo) return Progression.format(getProvider().getUnlocalisedName() + ".equal" + suffix, amount, variable);
        else return "INVALID SETUP";
    }

    @Override
    public boolean isSatisfied(IPlayerTeam team) {
        return isValidValue(ProgressionAPI.player.getDouble(team.getOwner(), variable));
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
