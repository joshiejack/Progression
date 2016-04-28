package joshie.progression.json;

import joshie.progression.helpers.StackHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.UUID;

public class DataTab {
    public DataTab() {}
    public DataTab(UUID uuid, String display, int sortIndex, List<DataCriteria> criteria, boolean isVisible, ItemStack stack) {
        this.uuid = uuid;
        this.displayName = display;
        this.sortIndex = sortIndex;
        this.criteria = criteria;
        this.isVisible = isVisible;
        this.stack = StackHelper.getStringFromStack(stack);
    }
    
    UUID uuid;
    String displayName;
    int sortIndex;
    boolean isVisible;
    String stack;
    List<DataCriteria> criteria;

    private transient ItemStack theStack;
    public ItemStack getIcon() {
        if (theStack == null) {
            theStack = StackHelper.getStackFromString(stack);
        }

        if (theStack == null) theStack = new ItemStack(Items.book);

        //Validation yo
        return theStack;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return displayName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataTab dataTab = (DataTab) o;
        return uuid != null ? uuid.equals(dataTab.uuid) : dataTab.uuid == null;

    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }
}
