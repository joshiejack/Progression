package joshie.progression.lib;

import java.util.UUID;

public class CriteriaNotFoundException extends NullPointerException {
    public CriteriaNotFoundException(UUID name) {
        super("The following criteria does not actually exist: " + name);
    }
}
