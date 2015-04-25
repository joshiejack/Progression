package joshie.crafting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class Tab {
    private String uniqueName;
    private String displayName;
    private boolean isVisible;
    private ItemStack stack;
    private int sortIndex;
    
    private List<Criteria> criteria = new ArrayList();
    
    public Tab setUniqueName(String unique) {
        this.uniqueName = unique;
        return this;
    }

    public Tab setDisplayName(String name) {
        this.displayName = name;
        return this;
    }

    public Tab addCriteria(Criteria... critera) {        
        for (Criteria c: critera) {
            criteria.add(c);
        }
        
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
    
    public List<Criteria> getCriteria() {
        return criteria;
    }

    public boolean isVisible() {
        return isVisible;
    }
    
    public ItemStack getStack() {
        return stack;
    }
    
    public int getSortIndex() {
        return sortIndex;
    }
}
