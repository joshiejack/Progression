package joshie.progression.api.criteria;

/** This is a reward provider, for storing information about rewards,
 *  that are unrelated to rewards themselves.
 */
public interface IFilterProvider extends IRuleProvider<IFilter> {
    /** Return the master object this is attached to**/
    public IRuleProvider getMaster();

    /** Returns the localised name **/
    public String getLocalisedName();
}
