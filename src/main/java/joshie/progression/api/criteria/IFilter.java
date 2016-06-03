package joshie.progression.api.criteria;

import net.minecraft.entity.player.EntityPlayer;

public interface IFilter<O, A> extends IRule<IFilterProvider> {
    /** Return true if the pass in object, matches this filter.
     *  Keep in mind this can pass in entities, itemstack,
     *  Lists or anything really, so make sure to validate
     *  all objects */
    public boolean matches(Object object);
    
    /** Returns a list of all things that match this filter
     *  May pass in something, or it can be null **/
    public O getRandom(EntityPlayer player);

    /** Called after generated to apply any special effects **/
    public void apply(A object);
    
    /** Returns the type of filter this is **/
    public IFilterType getType();
}
