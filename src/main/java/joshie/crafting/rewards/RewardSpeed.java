package joshie.crafting.rewards;

import java.util.UUID;

import joshie.crafting.api.Bus;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.IReward;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.plugins.minetweaker.Rewards;
import minetweaker.MineTweakerAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@ZenClass("mods.craftcontrol.rewards.Speed")
public class RewardSpeed extends RewardBase {
	private float speed;
	
	public RewardSpeed() {
		super("speed");
	}
	
	@Override
	public Bus getBusType() {
		return Bus.FORGE;
	}
	
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entity;
            if (player.worldObj.isRemote) {
    			float speed = CraftingAPI.players.getPlayerData(player).getAbilities().getSpeed();
    			if (speed > 0 && player.onGround && !player.isInWater() && player.isSprinting() && ClientHelper.isForwardPressed()) {
    				player.moveFlying(0F, 1.0F, speed);
    			}
    		}
		}
	}
	
	@ZenMethod
	public void add(String unique, double speed) {
		RewardSpeed reward = new RewardSpeed();
		reward.speed = Float.parseFloat("" + speed);		
		MineTweakerAPI.apply(new Rewards(unique, reward));
	}
	
	@Override
	public IReward deserialize(JsonObject data) {
		RewardSpeed reward = new RewardSpeed();
		reward.speed = data.get("speed").getAsFloat();
		return reward;
	}

	@Override
	public void serialize(JsonObject elements) {
		elements.addProperty("speed", speed);
	}
	
	@Override
	public void reward(UUID uuid) {
		CraftingAPI.players.getServerPlayer(uuid).addSpeed(speed);
	}
	
	private static final ItemStack speedStack = new ItemStack(Items.potionitem, 1, 8194);

    @Override
    public ItemStack getIcon() {
        return speedStack;
    }
}
