package joshie.crafting.api;

import java.util.List;

import net.minecraft.item.ItemStack;

public interface ITab extends IHasUniqueName {
    public ITab addCriteria(ICriteria... critera);

    public ITab setDisplayName(String name);
    
    public ITab setVisibility(boolean visibility);
    
    public ITab setStack(ItemStack stack);

    public List<ICriteria> getCriteria();

    public String getDisplayName();
    
    public boolean isVisible();

    public ItemStack getStack();
}
