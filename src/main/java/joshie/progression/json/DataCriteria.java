package joshie.progression.json;

import joshie.progression.helpers.StackHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.UUID;

public class DataCriteria {
    long timestamp;
    UUID uuid;
    String displayName;
    String displayStack;
    List<DataTrigger> triggers;
    List<DataGeneric> rewards;
    UUID[] prereqs;
    UUID[] conflicts;
    int repeatable;
    boolean infinite;
    int tasksRequired;
    boolean allTasks;
    int x;
    int y;
    boolean isVisible;
    boolean displayAchievement;
    public int rewardsGiven;
    public boolean allRewards;

    private transient ItemStack theStack;
    public ItemStack getIcon() {
        if (theStack == null) {
            theStack = StackHelper.getStackFromString(displayStack);
        }

        if (theStack == null) theStack = new ItemStack(Items.book);

        //Validation yo
        return theStack;
    }

    public String getName() {
        return displayName;
    }

    public UUID getUUID() {
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataCriteria criteria = (DataCriteria) o;
        return uuid != null ? uuid.equals(criteria.uuid) : criteria.uuid == null;

    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }
}
