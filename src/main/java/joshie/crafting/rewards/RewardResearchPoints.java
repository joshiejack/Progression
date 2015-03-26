package joshie.crafting.rewards;

import java.util.UUID;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.IReward;
import joshie.crafting.plugins.minetweaker.Rewards;
import minetweaker.MineTweakerAPI;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.google.gson.JsonObject;

@ZenClass("mods.craftcontrol.rewards.ResearchPoints")
public class RewardResearchPoints extends RewardBase {
    private int amount = 1;
    
    public RewardResearchPoints() {
        super("researchPoints");
    }
    
    @ZenMethod
    public void add(String unique, int amount) {
        RewardResearchPoints reward = new RewardResearchPoints();
        reward.amount = Math.max(1, amount);
        MineTweakerAPI.apply(new Rewards(unique, reward));
    }
    
    @Override
    public IReward deserialize(JsonObject data) {
        RewardResearchPoints reward = new RewardResearchPoints();
        if (data.get("amount") != null) {
            reward.amount = Math.max(1, data.get("amount").getAsInt());
        }

        return reward;
    }

    @Override
    public void serialize(JsonObject elements) {
        if (amount > 1) {
            elements.addProperty("amount", amount);
        }
    }
        
    @Override
    public void reward(UUID uuid) {
        CraftingAPI.players.getServerPlayer(uuid).addResearchPoints(amount);
    }
    
    //TODO: Replace this with a research icon
    @Override
    public ItemStack getIcon() {
        return new ItemStack(Items.potionitem);
    }
}
