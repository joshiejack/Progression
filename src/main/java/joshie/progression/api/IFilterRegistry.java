package joshie.progression.api;

import joshie.progression.api.criteria.IProgressionFilterSelector;

public interface IFilterRegistry {
    /** Returns a filter selector that will only select blocks **/
    public IProgressionFilterSelector getBlockFilter();
    
    /** Returns a filter selector that will only select entities **/
    public IProgressionFilterSelector getEntityFilter();
    
    /** Returns a filter selector that will only select potions **/
    public IProgressionFilterSelector getPotionFilter();
    
    /** Returns a filter selector that will only select locations **/
    public IProgressionFilterSelector getLocationFilter();
    
    /** Returns a filter selector that will select blocks and items **/
    public IProgressionFilterSelector getItemStackFilter();
    
    /** Returns a filter selector that will select actions **/
    public IProgressionFilterSelector getCraftingFilter();
}
