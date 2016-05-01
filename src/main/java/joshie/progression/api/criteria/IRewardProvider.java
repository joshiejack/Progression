package joshie.progression.api.criteria;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;

import java.util.List;

/** This is a reward provider, for storing information about rewards,
 *  that are unrelated to rewards themselves.
 */
public interface IRewardProvider extends IRuleProvider<IReward> {
    /** Return the criteria this reward is attached to **/
    public ICriteria getCriteria();

    /** Returns the icon for this reward **/
    public ItemStack getIcon();

    /** Returns the localised name **/
    public String getLocalisedName();

    /** Adds a tooltip **/
    public void addTooltip(List list);

    /** Set the icon **/
    public IRewardProvider setIcon(ItemStack stack);

    /** Returns true if only one of these rewards can be claimed per team **/
    public boolean isOnePerTeam();

    /** Returns whether this reward must be claimed **/
    public boolean mustClaim();

    /** Reads neccessary data about this provider from the json **/
    public void readFromJSON(JsonObject data);

    /** Writes neccessary data about this provider to the json **/
    public void writeToJSON(JsonObject data);
}
