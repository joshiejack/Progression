package joshie.progression.criteria;

import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.ITab;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Tab implements ITab {
    private UUID uuid;
    private String displayName;
    private boolean isVisible;
    private ItemStack stack;
    private int sortIndex;
    
    private List<ICriteria> criteria = new ArrayList();
    
    public Tab setUniqueName(UUID uuid) {
        this.uuid = uuid;
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

    @Override
    public UUID getUniqueID() {
        return uuid;
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
        return uuid != null ? uuid.equals(tab.uuid) : tab.uuid == null;

    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }
}
