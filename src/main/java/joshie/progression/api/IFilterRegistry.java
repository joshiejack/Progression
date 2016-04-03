package joshie.progression.api;

import joshie.progression.api.criteria.IFilterType;

public interface IFilterRegistry {
    /** Returns a filter selector that will only select blocks **/
    public IFilterType getBlockFilter();
    
    /** Returns a filter selector that will only select entities **/
    public IFilterType getEntityFilter();
    
    /** Returns a filter selector that will only select potions **/
    public IFilterType getPotionFilter();
    
    /** Returns a filter selector that will only select locations **/
    public IFilterType getLocationFilter();
    
    /** Returns a filter selector that will select blocks and items **/
    public IFilterType getItemStackFilter();
    
    /** Returns a filter selector that will select actions **/
    public IFilterType getCraftingFilter();
}
