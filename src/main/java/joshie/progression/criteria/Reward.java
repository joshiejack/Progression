package joshie.progression.criteria;

import com.google.gson.JsonObject;
import joshie.progression.Progression;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.api.criteria.IReward;
import joshie.progression.api.criteria.IRewardProvider;
import joshie.progression.api.special.*;
import joshie.progression.helpers.JSONHelper;
import joshie.progression.helpers.SplitHelper;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.UUID;

public class Reward implements IRewardProvider {
    private final IReward reward;
    private final String unlocalised;
    private final int color;

    private ICriteria criteria;
    private UUID uuid;

    private ItemStack stack;
    public boolean isVisible = true;
    public boolean mustClaim = false;

    //Dummy constructor for storing the default values
    public Reward(IReward reward, String unlocalised, int color) {
        this.reward = reward;
        this.unlocalised = unlocalised;
        this.color = color;
        this.reward.setProvider(this);
    }

    public Reward (ICriteria criteria, UUID uuid, IReward reward, ItemStack stack, String unlocalised, int color) {
        this.criteria = criteria;
        this.uuid = uuid;
        this.reward = reward;
        this.unlocalised = unlocalised;
        this.color = color;
        this.stack = stack;
        this.reward.setProvider(this);
    }

    @Override
    public ICriteria getCriteria() {
        return criteria;
    }

    @Override
    public IReward getProvided() {
        return reward;
    }

    @Override
    public String getUnlocalisedName() {
        return unlocalised;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public ItemStack getIcon() {
        return reward instanceof ICustomIcon ? ((ICustomIcon)reward).getIcon() : stack;
    }

    @Override
    public String getLocalisedName() {
        return reward instanceof ICustomDisplayName ? ((ICustomDisplayName)reward).getDisplayName() : Progression.translate(getUnlocalisedName());
    }

    @Override
    public String getDescription() {
        return reward instanceof ICustomDescription ? ((ICustomDescription)reward).getDescription() : Progression.format(getUnlocalisedName() + ".description");
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return reward instanceof ICustomWidth ? ((ICustomWidth)reward).getWidth(mode) : 100;
    }

    @Override
    public void addTooltip(List list) {
        if (reward instanceof ICustomTooltip) ((ICustomTooltip)reward).addTooltip(list);
        else{
            for (String s : SplitHelper.splitTooltip(getDescription(), 42)) {
                list.add(s);
            }
        }
    }

    @Override
    public UUID getUniqueID() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }

        return uuid;
    }

    @Override
    public IRewardProvider setIcon(ItemStack stack) {
        this.stack = stack;
        return this;
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public boolean mustClaim() {
        return mustClaim;
    }

    @Override
    public void readFromJSON(JsonObject data) {
        isVisible = JSONHelper.getBoolean(data, "isVisible", true);
        mustClaim = JSONHelper.getBoolean(data, "mustClaim", false);
    }

    @Override
    public void writeToJSON(JsonObject data) {
        JSONHelper.setBoolean(data, "isVisible", isVisible, true);
        JSONHelper.setBoolean(data, "mustClaim", mustClaim, false);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof IRewardProvider)) return false;

        IRewardProvider that = (IRewardProvider) o;
        return getUniqueID() != null ? getUniqueID().equals(that.getUniqueID()) : that.getUniqueID() == null;

    }

    @Override
    public int hashCode() {
        return getUniqueID() != null ? getUniqueID().hashCode() : 0;
    }
}
