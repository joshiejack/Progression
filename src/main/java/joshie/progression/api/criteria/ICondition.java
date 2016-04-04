package joshie.progression.api.criteria;

import joshie.progression.api.IPlayerTeam;

public interface ICondition extends IRule<IConditionProvider> {
    /** Should true true if this condition is satisfied
     * @param team this is information about the team **/
    public boolean isSatisfied(IPlayerTeam team);
}
