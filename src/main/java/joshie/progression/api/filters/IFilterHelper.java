package joshie.progression.api.filters;

public interface IFilterHelper {
    /** Returns a filter selector that will only select blocks **/
    public IFilterSelectorFilter getBlockFilter();
    
    /** Returns a filter selector that will only select entities **/
    public IFilterSelectorFilter getEntityFilter();
    
    /** Returns a filter selector that will only select potions **/
    public IFilterSelectorFilter getPotionFilter();
    
    /** Returns a filter selector that will only select locations **/
    public IFilterSelectorFilter getLocationFilter();
    
    /** Returns a filter selector that will select blocks and items **/
    public IFilterSelectorFilter getItemStackFilter();
}
