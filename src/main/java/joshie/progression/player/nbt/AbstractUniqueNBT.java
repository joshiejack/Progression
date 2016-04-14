package joshie.progression.player.nbt;

import joshie.progression.helpers.NBTHelper.ICollectionHelper;

import java.util.Collection;

public abstract class AbstractUniqueNBT<T> implements ICollectionHelper {
    private Collection collection;

    public AbstractUniqueNBT setCollection(Collection collection) {
        this.collection = collection;
        return this;
    }

    @Override
    public Collection getSet() {
        return collection;
    }
}
