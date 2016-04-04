package joshie.progression.criteria;

import joshie.progression.Progression;
import joshie.progression.api.criteria.IFilter;
import joshie.progression.api.criteria.IFilterProvider;
import joshie.progression.api.criteria.IRuleProvider;
import joshie.progression.api.special.DisplayMode;
import joshie.progression.api.special.ICustomDescription;
import joshie.progression.api.special.ICustomDisplayName;
import joshie.progression.api.special.ICustomWidth;

import java.util.UUID;

public class Filter implements IFilterProvider {
    private final IFilter filter;
    private final String unlocalised;
    private final int color;

    private IRuleProvider provider;
    private UUID uuid;

    //Dummy constructor for storing the default values
    public Filter(IFilter filter, String unlocalised, int color) {
        this.filter = filter;
        this.unlocalised = unlocalised;
        this.color = color;
        this.filter.setProvider(this);
    }

    public Filter(IRuleProvider provider, UUID uuid, IFilter filter, String unlocalised, int color) {
        this.provider = provider;
        this.uuid = uuid;
        this.filter = filter;
        this.unlocalised = unlocalised;
        this.color = color;
        this.filter.setProvider(this);
    }

    @Override
    public IRuleProvider getMaster() {
        return provider;
    }

    @Override
    public IFilter getProvided() {
        return filter;
    }

    @Override
    public String getUnlocalisedName() {
        return unlocalised;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public String getLocalisedName() {
        return filter instanceof ICustomDisplayName ? ((ICustomDisplayName)filter).getDisplayName() : Progression.translate(getUnlocalisedName());
    }

    @Override
    public String getDescription() {
        return filter instanceof ICustomDescription ? ((ICustomDescription)filter).getDescription() : Progression.format(getUnlocalisedName() + ".description");
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return filter instanceof ICustomWidth ? ((ICustomWidth)filter).getWidth(mode) : 100;
    }

    @Override
    public UUID getUniqueID() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }

        return uuid;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof IFilterProvider)) return false;

        IFilterProvider that = (IFilterProvider) o;
        return getUniqueID() != null ? getUniqueID().equals(that.getUniqueID()) : that.getUniqueID() == null;

    }

    @Override
    public int hashCode() {
        return getUniqueID() != null ? getUniqueID().hashCode() : 0;
    }
}
