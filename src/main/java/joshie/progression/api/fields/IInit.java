package joshie.progression.api.fields;

/** Implement this on things which need to have init on load
 *  or when fields are changed */
public interface IInit {
    /** Perform init stuff **/
    public void init();
}
