package joshie.progression.player.nbt;

import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.ITrigger;
import joshie.progression.handlers.APIHandler;
import joshie.progression.helpers.TriggerHelper;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.UUID;

public class TriggerNBT extends AbstractUniqueNBT {
    public static final TriggerNBT INSTANCE = new TriggerNBT();

    @Override
    public NBTBase write(Object s) {
        NBTTagCompound tag = new NBTTagCompound();
        ITrigger t = ((ITrigger) s);
        tag.setString("Criteria", t.getCriteria().getUniqueID().toString());
        tag.setInteger("Value", TriggerHelper.getInternalID(t));
        return tag;
    }

    @Override
    public Object read(NBTTagList list, int i) {
        NBTTagCompound tag = list.getCompoundTagAt(i);
        ICriteria criteria = APIHandler.getCriteriaFromName(UUID.fromString(tag.getString("Criteria")));
        if (criteria == null) return null;
        int value = tag.getInteger("Value");
        if (value < criteria.getTriggers().size()) {
            return (ITrigger) criteria.getTriggers().get(value);
        } else return null;
    }
}
