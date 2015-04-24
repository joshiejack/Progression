package joshie.crafting;

import java.util.ArrayList;
import java.util.List;

import joshie.crafting.api.ITab;
import net.minecraft.item.ItemStack;

public class CraftingTab implements ITab {
    private String uniqueName;
    private String displayName;
    private boolean isVisible;
    private ItemStack stack;
    private int sortIndex;
    
    private List<Criteria> criteria = new ArrayList();
    @Override
    public ITab setUniqueName(String unique) {
        this.uniqueName = unique;
        return this;
    }

    @Override
    public ITab setDisplayName(String name) {
        this.displayName = name;
        return this;
    }

    @Override
    public ITab addCriteria(Criteria... critera) {        
        for (Criteria c: critera) {
            criteria.add(c);
        }
        
        return this;
    }
    
    @Override
    public ITab setVisibility(boolean visibility) {
        this.isVisible = visibility;
        return this;
    }
    
    @Override
    public ITab setStack(ItemStack stack) {
        this.stack = stack;
        return this;
    }
    
    @Override
    public ITab setSortIndex(int index) {
        this.sortIndex = index;
        return this;
    }

    @Override
    public String getUniqueName() {
        return uniqueName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public List<Criteria> getCriteria() {
        return criteria;
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }
    
    @Override
    public ItemStack getStack() {
        return stack;
    }
    
    @Override
    public int getSortIndex() {
        return sortIndex;
    }
}
