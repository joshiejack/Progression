package joshie.progression.criteria.filters;

import joshie.progression.api.criteria.IFilter;
import joshie.progression.api.criteria.IFilterProvider;
import joshie.progression.api.criteria.IRuleProvider;

public abstract class FilterBase implements IFilter {
    private IFilterProvider provider;

    @Override
    public IFilterProvider getProvider() {
        return provider;
    }

    @Override
    public void setProvider(IRuleProvider provider) {
        this.provider = (IFilterProvider) provider;
    }

    @Override
    public boolean matches(Object object) {
        return false;
    }
}
