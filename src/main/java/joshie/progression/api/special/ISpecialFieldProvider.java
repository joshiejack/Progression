package joshie.progression.api.special;

import joshie.progression.api.criteria.IField;

import java.util.List;

/** Implement this on rewards, conditions, triggers, filters,
 *  where you wish to add special fields to be loaded, as well
 *  as or instead of the default loading */
public interface ISpecialFieldProvider {
    /** Add extra fields, If you wish to only use your new fields,
     *  Just clear the list. **/
    public void addSpecialFields(List<IField> fields, DisplayMode mode);
}
