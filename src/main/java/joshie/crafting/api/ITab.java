package joshie.crafting.api;

import java.util.List;

import joshie.crafting.Criteria;
import net.minecraft.item.ItemStack;

public interface ITab extends IHasUniqueName {
    public ITab addCriteria(Criteria... critera);

    public ITab setDisplayName(String name);
    
    public ITab setVisibility(boolean visibility);
    
    public ITab setStack(ItemStack stack);
    
    public ITab setSortIndex(int index);

    public List<Criteria> getCriteria();

    public String getDisplayName();
    
    public boolean isVisible();

    public ItemStack getStack();
    
    public int getSortIndex();
}
