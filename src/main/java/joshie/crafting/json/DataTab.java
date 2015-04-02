package joshie.crafting.json;

import java.util.List;

import joshie.crafting.helpers.StackHelper;
import net.minecraft.item.ItemStack;

public class DataTab {
    public DataTab() {}
    public DataTab(String unique, String display, int sortIndex, List<DataCriteria> criteria, boolean isVisible, ItemStack stack) {
        this.uniqueName = unique;
        this.displayName = display;
        this.sortIndex = sortIndex;
        this.criteria = criteria;
        this.isVisible = isVisible;
        this.stack = StackHelper.getStringFromStack(stack);
    }
    
    String uniqueName;
    String displayName;
    int sortIndex;
    boolean isVisible;
    String stack;
    List<DataCriteria> criteria;
}
