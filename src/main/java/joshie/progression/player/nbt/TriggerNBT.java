package joshie.progression.player.nbt;

import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.ITriggerProvider;
import joshie.progression.handlers.APIHandler;
import joshie.progression.helpers.NBTHelper.ICollectionHelper;
import joshie.progression.helpers.TriggerHelper;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.Collection;
import java.util.UUID;

public class TriggerNBT implements ICollectionHelper<ITriggerProvider> {
    public static final TriggerNBT INSTANCE = new TriggerNBT();
    private Collection collection;

    @Override
    public Collection getSet() {
        return collection;
    }

    public TriggerNBT setCollection(Collection collection) {
        this.collection = collection;
        return this;
    }

    @Override
    public NBTBase write(ITriggerProvider t) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Criteria", t.getCriteria().getUniqueID().toString());
        tag.setInteger("Value", TriggerHelper.getInternalID(t));
        return tag;
    }

    @Override
    public ITriggerProvider read(NBTTagList list, int i) {
        NBTTagCompound tag = list.getCompoundTagAt(i);
        ICriteria criteria = APIHandler.getServerCache().getCriteria(UUID.fromString(tag.getString("Criteria")));
        if (criteria == null) return null;
        int value = tag.getInteger("Value");
        if (value < criteria.getTriggers().size()) {
            return criteria.getTriggers().get(value);
        } else return null;
    }
}
