package joshie.progression.player.nbt;

import joshie.progression.api.criteria.ICriteria;
import joshie.progression.handlers.APIHandler;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.UUID;

public class CriteriaSet extends AbstractUniqueNBT {
    public static final CriteriaSet INSTANCE = new CriteriaSet();

    @Override
    public NBTBase write(Object s) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Criteria", ((ICriteria) s).getUniqueID().toString());
        return tag;
    }

    @Override
    public Object read(NBTTagList list, int i) {
        NBTTagCompound tag = list.getCompoundTagAt(i);
        return APIHandler.getCriteriaFromName(UUID.fromString(tag.getString("Criteria")));
    }
}
