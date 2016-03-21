package joshie.progression.criteria;

import java.util.ArrayList;
import java.util.List;

import joshie.progression.api.ICriteria;
import joshie.progression.api.ITab;
import joshie.progression.api.event.TabVisibleEvent;
import joshie.progression.helpers.MCClientHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class Tab implements ITab {
    private String uniqueName;
    private String displayName;
    private boolean isVisible;
    private ItemStack stack;
    private int sortIndex;
    
    private List<ICriteria> criteria = new ArrayList();
    
    public Tab setUniqueName(String unique) {
        this.uniqueName = unique;
        return this;
    }

    public Tab setDisplayName(String name) {
        this.displayName = name;
        return this;
    }

    public Tab addCriteria(ICriteria... critera) {        
        for (ICriteria c: critera) {
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
    
    @Override
    public List<ICriteria> getCriteria() {
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
}
