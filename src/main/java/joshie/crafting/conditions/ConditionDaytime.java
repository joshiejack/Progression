package joshie.crafting.conditions;

import java.util.UUID;

import joshie.crafting.api.ICondition;
import joshie.crafting.plugins.minetweaker.Conditions;
import minetweaker.MineTweakerAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.google.gson.JsonObject;

@ZenClass("mods.craftcontrol.conditions.Daytime")
public class ConditionDaytime extends ConditionBase {
	private boolean isDaytime = true;
	
	public ConditionDaytime() {
		super("daytime");
	}
	
	@ZenMethod
	public void add(String unique) {
		MineTweakerAPI.apply(new Conditions(unique, new ConditionDaytime()));
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
