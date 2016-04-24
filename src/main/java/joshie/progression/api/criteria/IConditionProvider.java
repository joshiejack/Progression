package joshie.progression.api.criteria;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;

/** This is a condition provider, for storing information about conditions,
 *  that are unrelated to conditions themselves.
 */
public interface IConditionProvider extends IRuleProvider<ICondition> {
    /** Return the trigger this condition is attached to **/
    public ITriggerProvider getTrigger();

    /** Whether this condition checks for true or false **/
    public boolean isInverted();

    /** Returns the icon for this condition **/
    public ItemStack getIcon();

    /** Returns the localised name **/
    public String getLocalisedName();

    /** Set the icon **/
    public IConditionProvider setIcon(ItemStack stack);

    /** Sets the satisfaction for conditions that were synced **/
    public void setSatisfied(boolean isTrue);

    /** Returns whether this is true or not **/
    public boolean isSatisfied();

    /** Reads neccessary data about this provider from the json **/
    public void readFromJSON(JsonObject data);

    /** Writes neccessary data about this provider to the json **/
    public void writeToJSON(JsonObject data);
}
