package joshie.crafting.conditions;

import java.util.UUID;

import joshie.crafting.api.ICondition;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.google.gson.JsonObject;

public class ConditionDaytime extends ConditionBase {
	private boolean isDaytime = true;
	
	public ConditionDaytime() {
		super("daytime");
	}
	
	@Override
	public boolean isSatisfied(World world, EntityPlayer player, UUID uuid) {
		return world.isDaytime() == isDaytime;
	}

	@Override
	public ICondition deserialize(JsonObject data) {
		return new ConditionDaytime();
	}

	@Override
	public void serialize(JsonObject elements) {}
}
