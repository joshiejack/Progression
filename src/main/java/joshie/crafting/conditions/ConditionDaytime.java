package joshie.crafting.conditions;

import java.util.UUID;

import joshie.crafting.api.ICondition;
import joshie.crafting.minetweaker.Conditions;
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
		super("time");
	}
	
	@ZenMethod
	public void add(String unique, boolean isDaytime) {
		ConditionDaytime condition = new ConditionDaytime();
		condition.isDaytime = isDaytime;
		MineTweakerAPI.apply(new Conditions(unique, condition));
	}

	@Override
	public boolean isSatisfied(World world, EntityPlayer player, UUID uuid) {
		return world.isDaytime() == isDaytime;
	}

	@Override
	public ICondition deserialize(JsonObject data) {
		ConditionDaytime time = new ConditionDaytime();
		if (data.get("Night") != null) {
			time.isDaytime = Boolean.parseBoolean("Night");
		}
		
		return time;
	}

	@Override
	public void serialize(JsonObject elements) {
		if (!isDaytime) {
			elements.addProperty("Night", true);
		}
	}
}
