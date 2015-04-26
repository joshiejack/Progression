package joshie.progression.player.nbt;

import java.util.Collection;

import joshie.progression.helpers.NBTHelper.ICollectionHelper;

public abstract class AbstractUniqueNBT implements ICollectionHelper {	
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
