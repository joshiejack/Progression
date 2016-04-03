package joshie.progression.criteria.conditions;

import joshie.progression.Progression;
import joshie.progression.api.IPlayerTeam;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.items.ItemCriteria;

public class ConditionPoints extends ConditionBase {
    public String variable = "gold";
    public double amount = 1D;
    public boolean greaterThan = true;
    public boolean isEqualTo = true;
    public boolean lesserThan = false;

    public ConditionPoints() {
        super(ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.ifHasPoints), "points", 0xFF00FFBF);
    }

    private boolean isValidValue(double total) {
        if (greaterThan && total > amount) return true;
        if (isEqualTo && total == amount) return true;
        if (lesserThan && total < amount) return true;

        //FALSE BABY!!!
        return false;
    }

    @Override
    public boolean isSatisfied(IPlayerTeam team) {
        return isValidValue(ProgressionAPI.player.getDouble(team.getOwner(), variable));
    }

    @Override
    public String getConditionDescription() {
        String suffix = inverted ? ".inverted" : "";
        if (greaterThan && isEqualTo) return Progression.format(getUnlocalisedName() + ".greater.equal" + suffix, amount, variable);
        else if (lesserThan && isEqualTo) return Progression.format(getUnlocalisedName() + ".lesser.equal" + suffix, amount, variable);
        else if (greaterThan) return Progression.format(getUnlocalisedName() + ".greater" + suffix, amount, variable);
        else if (lesserThan) return Progression.format(getUnlocalisedName() + ".lesser" + suffix, amount, variable);
        else if (isEqualTo) return Progression.format(getUnlocalisedName() + ".equal" + suffix, amount, variable);
        else return "INVALID SETUP";
    }
}
