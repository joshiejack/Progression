package joshie.progression.criteria.conditions;

import joshie.progression.api.criteria.ICondition;
import joshie.progression.api.criteria.IConditionProvider;

public abstract class ConditionBase implements ICondition {
    private IConditionProvider provider;

    @Override
    public void setProvider(IConditionProvider provider) {
        this.provider = provider;
    }

    @Override
    public IConditionProvider getProvider() {
        return provider;
    }
}
