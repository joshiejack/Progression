package joshie.progression.api.criteria;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;

import java.util.List;

/** This is a reward provider, for storing information about rewards,
 *  that are unrelated to rewards themselves.
 */
public interface ITriggerProvider extends IRuleProvider<ITrigger> {
    /** Returns a copy of this trigger provider **/
    public ITriggerProvider copy();

    /** Return the criteria this reward is attached to **/
    public ICriteria getCriteria();

    /** Return the list of conditions for this trigger **/
    public List<IConditionProvider> getConditions();

    /** Returns the icon for this reward **/
    public ItemStack getIcon();

    /** Returns the localised name **/
    public String getLocalisedName();

    /** Whether this trigger is cancelable **/
    public boolean isCancelable();

    /** Whether this trigger is currently cancel enabled **/
    public boolean isCanceling();

    /** Set the icon **/
    public ITriggerProvider setIcon(ItemStack stack);

    /** Marks this trigger as cancelable **/
    public ITriggerProvider setCancelable();

    /** Reads neccessary data about this provider from the json **/
    public void readFromJSON(JsonObject data);

    /** Writes neccessary data about this provider to the json **/
    public void writeToJSON(JsonObject data);
}
