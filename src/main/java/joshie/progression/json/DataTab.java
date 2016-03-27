package joshie.progression.json;

import joshie.progression.helpers.StackHelper;
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
}
