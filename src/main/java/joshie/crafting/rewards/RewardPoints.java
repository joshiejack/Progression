package joshie.crafting.rewards;

import java.util.UUID;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.IReward;
import joshie.crafting.plugins.minetweaker.Rewards;
import minetweaker.MineTweakerAPI;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.google.gson.JsonObject;

@ZenClass("mods.craftcontrol.rewards.ResearchPoints")
public class RewardPoints extends RewardBase {
    private String name;
    private int amount = 1;
    
    public RewardPoints() {
        super("researchPoints");
    }
    
    @ZenMethod
    public void add(String unique, String name, @Optional int amount) {
        RewardPoints reward = new RewardPoints();
        reward.name = name;
        reward.amount = Math.max(1, amount);
        MineTweakerAPI.apply(new Rewards(unique, reward));
    }
    
    @Override
    public IReward deserialize(JsonObject data) {
        RewardPoints reward = new RewardPoints();
        reward.name = data.get("name").getAsString();
        if (data.get("amount") != null) {
            reward.amount = Math.max(1, data.get("amount").getAsInt());
        }

        return reward;
    }

    @Override
    public void serialize(JsonObject elements) {
        elements.addProperty("name", name);
        if (amount > 1) {
            elements.addProperty("amount", amount);
        }
    }
        
    @Override
    public void reward(UUID uuid) {
        CraftingAPI.players.getServerPlayer(uuid).addPoints(name, amount);
    }
    
    //TODO: Replace this with a research icon
    @Override
    public ItemStack getIcon() {
        return new ItemStack(Items.potionitem);
    }
}
