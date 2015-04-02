package joshie.crafting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import joshie.crafting.api.ICriteria;
import joshie.crafting.api.IHasUniqueName;
import joshie.crafting.api.ITab;

public class CraftingTab implements ITab {
    private String uniqueName;
    private String displayName;
    private boolean isVisible;
    private ItemStack stack;
    private int sortIndex;
    
    private List<ICriteria> criteria = new ArrayList();
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
    public ITab addCriteria(ICriteria... critera) {        
        for (ICriteria c: critera) {
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
    public List<ICriteria> getCriteria() {
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
