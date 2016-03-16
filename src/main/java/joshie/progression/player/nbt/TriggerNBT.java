package joshie.progression.player.nbt;

import joshie.progression.api.ICriteria;
import joshie.progression.api.ITriggerType;
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
        ITriggerType t = ((ITriggerType) s);
        tag.setString("Criteria", t.getCriteria().getUniqueName());
        tag.setInteger("Value", TriggerHelper.getInternalID(t));
        return tag;
    }

    @Override
    public Object read(NBTTagList list, int i) {
        NBTTagCompound tag = list.getCompoundTagAt(i);
        ICriteria criteria = APIHandler.getCriteriaFromName(tag.getString("Criteria"));
        if (criteria == null) return null;
        int value = tag.getInteger("Value");
        if (value < criteria.getTriggers().size()) {
            return (ITriggerType) criteria.getTriggers().get(value);
        } else return null;
    }
}
