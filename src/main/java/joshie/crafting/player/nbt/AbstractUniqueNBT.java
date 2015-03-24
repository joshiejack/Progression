package joshie.crafting.player.nbt;

import java.util.Collection;

import joshie.crafting.api.IHasUniqueName;
import joshie.crafting.helpers.NBTHelper.ICollectionHelper;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;

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

	@Override
	public NBTBase write(Object s) {
		return new NBTTagString(((IHasUniqueName)s).getUniqueName());
	}
}
