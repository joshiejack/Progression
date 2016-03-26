package joshie.progression.criteria;

import joshie.progression.api.criteria.IProgressionCriteria;
import joshie.progression.api.criteria.IProgressionTab;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Tab implements IProgressionTab {
    private String uniqueName;
    private String displayName;
    private boolean isVisible;
    private ItemStack stack;
    private int sortIndex;
    
    private List<IProgressionCriteria> criteria = new ArrayList();
    
    public Tab setUniqueName(String unique) {
        this.uniqueName = unique;
        return this;
    }

    public Tab setDisplayName(String name) {
        this.displayName = name;
        return this;
    }
   
    public Tab setVisibility(boolean visibility) {
        this.isVisible = visibility;
        return this;
    }
    
    public Tab setStack(ItemStack stack) {
        this.stack = stack;
        return this;
    }
    
    public Tab setSortIndex(int index) {
        this.sortIndex = index;
        return this;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public List<IProgressionCriteria> getCriteria() {
        return criteria;
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }
    
    public ItemStack getStack() {
        return stack;
    }
    
    public int getSortIndex() {
        return sortIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tab tab = (Tab) o;
        return uniqueName != null ? uniqueName.equals(tab.uniqueName) : tab.uniqueName == null;

    }

    @Override
    public int hashCode() {
        return uniqueName != null ? uniqueName.hashCode() : 0;
    }
}
