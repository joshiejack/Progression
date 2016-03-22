package joshie.progression.player.nbt;

import joshie.progression.api.criteria.IProgressionCriteria;
import joshie.progression.api.criteria.IProgressionTrigger;
import joshie.progression.handlers.APIHandler;
import joshie.progression.helpers.TriggerHelper;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import scala.swing.Action.Trigger;

public class TriggerNBT extends AbstractUniqueNBT {
    public static final TriggerNBT INSTANCE = new TriggerNBT();

    @Override
    public NBTBase write(Object s) {
        NBTTagCompound tag = new NBTTagCompound();
        IProgressionTrigger t = ((IProgressionTrigger) s);
        tag.setString("Criteria", t.getCriteria().getUniqueName());
        tag.setInteger("Value", TriggerHelper.getInternalID(t));
        return tag;
    }

    @Override
    public Object read(NBTTagList list, int i) {
        NBTTagCompound tag = list.getCompoundTagAt(i);
        IProgressionCriteria criteria = APIHandler.getCriteriaFromName(tag.getString("Criteria"));
        if (criteria == null) return null;
        int value = tag.getInteger("Value");
        if (value < criteria.getTriggers().size()) {
            return (IProgressionTrigger) criteria.getTriggers().get(value);
        } else return null;
    }
}
