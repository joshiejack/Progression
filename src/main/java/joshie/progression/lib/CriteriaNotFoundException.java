package joshie.progression.lib;

public class CriteriaNotFoundException extends NullPointerException {
    public CriteriaNotFoundException(String name) {
        super("The following condition does not actually exist: " + name);
    }
}
