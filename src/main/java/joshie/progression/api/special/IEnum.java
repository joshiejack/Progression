package joshie.progression.api.special;

/** Implement this on fieldproviders which use an enum
 *  to update and return the next one */
public interface IEnum {
    /** Grab the next enum **/
    public Enum next(String name);

    /** Returns true if this field is an enum **/
    boolean isEnum(String name);
}
