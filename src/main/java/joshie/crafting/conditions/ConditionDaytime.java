package joshie.crafting.conditions;

import java.util.List;
import java.util.UUID;

import joshie.crafting.api.ICondition;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.google.gson.JsonObject;

public class ConditionDaytime extends ConditionBase {	
	public ConditionDaytime() {
		super("Is daytime", 0xFFFFFF00, "daytime");
	}
	
	@Override
	public boolean isSatisfied(World world, EntityPlayer player, UUID uuid) {
		return world.isDaytime() == !isInverted();
	}

	@Override
	public ICondition deserialize(JsonObject data) {
		return new ConditionDaytime();
	}

	@Override
	public void serialize(JsonObject elements) {}

    @Override
    public ICondition newInstance() {
        return new ConditionDaytime();
    }

    @Override
    public void addToolTip(List<String> toolTip) {
        if (inverted) {
            toolTip.add("    At Night");
        } toolTip.add("    In the Day");
    }
}
