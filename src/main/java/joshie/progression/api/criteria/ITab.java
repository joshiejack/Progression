package joshie.progression.api.criteria;

import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.UUID;

public interface ITab {
    /** Returns a list of all the criteria in this tab **/
    public List<ICriteria> getCriteria();
    
    /** Returns the unique name for this tab **/
    public UUID getUniqueID();
    
    /** Returns the display name for this tab **/
    public String getDisplayName();
    
    /** Returns the display stack for this tab **/
    public ItemStack getStack();
    
    /** Returns the sort index for this tab **/
    public int getSortIndex();

    /** Whether this tab is currently visible or not **/
    public boolean isVisible();

    public ITab setDisplayName(String name);
    public ITab setStack(ItemStack icon);
    public ITab setSortIndex(int value);
    public ITab setVisibility(boolean isVisible);
}
