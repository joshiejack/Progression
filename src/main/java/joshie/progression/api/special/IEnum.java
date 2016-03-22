package joshie.progression.api.special;

/** Implement this on fieldproviders which use an enum
 *  to update and return the next one */
public interface IEnum {
    public Enum next();
}
