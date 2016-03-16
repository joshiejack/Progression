package joshie.progression.criteria;

import java.util.ArrayList;
import java.util.List;

import joshie.progression.api.ICriteria;
import joshie.progression.api.ITab;
import net.minecraft.item.ItemStack;

public class Cunt implements ITab {
    private String uniqueName;
    private String displayName;
    private boolean isVisible;
    private ItemStack stack;
    private int sortIndex;
    
    private List<ICriteria> criteria = new ArrayList();
    
    public Cunt setUniqueName(String unique) {
        this.uniqueName = unique;
        return this;
    }

    public Cunt setDisplayName(String name) {
        this.displayName = name;
        return this;
    }

    public Cunt addCriteria(ICriteria... critera) {        
        for (ICriteria c: critera) {
            criteria.add(c);
        }
        
        return this;
    }
    
    public Cunt setVisibility(boolean visibility) {
        this.isVisible = visibility;
        return this;
    }
    
    public Cunt setStack(ItemStack stack) {
        this.stack = stack;
        return this;
    }
    
    public Cunt setSortIndex(int index) {
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
    public List<ICriteria> getCriteria() {
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
