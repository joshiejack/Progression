package joshie.progression.player.nbt;

import joshie.progression.criteria.Criteria;
import joshie.progression.criteria.Trigger;
import joshie.progression.handlers.APIHandler;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TriggerNBT extends AbstractUniqueNBT {
    public static final TriggerNBT INSTANCE = new TriggerNBT();

    @Override
    public NBTBase write(Object s) {
        NBTTagCompound tag = new NBTTagCompound();
        Trigger t = ((Trigger) s);
        tag.setString("Criteria", t.getCriteria().uniqueName);
        tag.setInteger("Value", t.getInternalID());
        return tag;
    }

    @Override
    public Object read(NBTTagList list, int i) {
        NBTTagCompound tag = list.getCompoundTagAt(i);
        Criteria criteria = APIHandler.getCriteriaFromName(tag.getString("Criteria"));
        if (criteria == null) return null;
        int value = tag.getInteger("Value");
        if (value < criteria.triggers.size()) {
            return (Trigger) criteria.triggers.get(value);
        } else return null;
    }
}
