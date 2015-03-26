package joshie.crafting.rewards;

import java.util.UUID;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.IResearch;
import joshie.crafting.api.IReward;
import joshie.crafting.plugins.minetweaker.Rewards;
import minetweaker.MineTweakerAPI;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.google.gson.JsonObject;

@ZenClass("mods.craftcontrol.rewards.Research")
public class RewardResearch extends RewardBase {    
    private IResearch research;
    
    public RewardResearch() {
        super("research");
    }
    
    @ZenMethod
    public void add(String unique, String researchName) {
        RewardResearch reward = new RewardResearch();
        reward.research = (IResearch) CraftingAPI.registry.getTrigger(null, researchName, null);
        MineTweakerAPI.apply(new Rewards(unique, reward));
    }
    
    @Override
    public IReward deserialize(JsonObject data) {
        RewardResearch reward = new RewardResearch();
        reward.research = (IResearch) CraftingAPI.registry.getTrigger(null, data.get("researchName").getAsString(), null);
        return reward;
    }

    @Override
    public void serialize(JsonObject elements) {
        elements.addProperty("researchName", research.getUniqueName());
    }
        
    @Override
    public void reward(UUID uuid) {
        CraftingAPI.players.getServerPlayer(uuid).getMappings().fireAllTriggers(research.getTypeName(), research.getResearchName());
    }
    
    //TODO: Replace this with a research icon
    @Override
    public ItemStack getIcon() {
        return new ItemStack(Items.potionitem);
    }
}
