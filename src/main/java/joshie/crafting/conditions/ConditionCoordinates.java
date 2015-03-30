package joshie.crafting.conditions;

import java.util.UUID;

import joshie.crafting.api.ICondition;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.google.gson.JsonObject;

public class ConditionCoordinates extends ConditionBase {
	private boolean checkDimension = false; //Whether we check the dimension
	private boolean checkX = true; //Whether we check the x coordinate
	private boolean checkY = true; //Whether we check the y coordinate
	private boolean checkZ = true; //Whether we check the x coordinate
	private int radius = 0; //How much further to check than the block
	private int dimension, x, y, z; //The coordinates
	private boolean greaterThan = false; //If we are checking greater than
	private boolean lessThan = false; //If we are checking less than
	
	public ConditionCoordinates() {
		super("coordinates");
	}

	@Override
	public boolean isSatisfied(World world, EntityPlayer player, UUID uuid) {
		if (player == null) return false;
		int playerDimension = player.worldObj.provider.dimensionId;
		int playerX = (int) player.posX;
		int playerY = (int) player.posY;
		int playerZ = (int) player.posZ;
		
		if (checkDimension) if (playerDimension != dimension) return false;
		boolean xMatches = checkX ? false: true;
		boolean yMatches = checkY ? false: true;
		boolean zMatches = checkZ ? false: true;
		if (greaterThan || lessThan) {
			if (greaterThan) {
				if(playerX >= x) xMatches = true;
				if(playerY >= y) yMatches = true;
				if(playerZ >= z) zMatches = true;
			} else {
				if(playerX <= x) xMatches = true;
				if(playerY <= y) yMatches = true;
				if(playerZ <= z) zMatches = true;
			}
		} else {
			//Check the xCoordinate
			if (checkX) {
				for (int xCoord = playerX - radius; xCoord <= playerX + radius; xCoord++) {
					if (xCoord == x) {
						xMatches = true;
						break;
					}
				}
			}
			//Check the yCoordinate
			if (checkY) {
				for (int yCoord = playerY - radius; yCoord <= playerY + radius; yCoord++) {
					if (yCoord == y) {
						yMatches = true;
						break;
					}
				}
			}
			//Check the zCoordinate
			if (checkZ) {
				for (int zCoord = playerZ - radius; zCoord <= playerZ + radius; zCoord++) {
					if (zCoord == z) {
						zMatches = true;
						break;
					}
				}
			}
		}
		
		return xMatches && yMatches && zMatches;
	}

	@Override
	public ICondition deserialize(JsonObject data) {
		ConditionCoordinates condition = new ConditionCoordinates();
		if (data.get("dimension") != null) {
			condition.checkDimension = true;
			condition.dimension = data.get("dimension").getAsInt();
		}
		
		if (data.get("x") != null) {
			condition.checkX = true;
			condition.x = data.get("x").getAsInt();
		}
		
		if (data.get("y") != null) {
			condition.checkY = true;
			condition.y = data.get("y").getAsInt();
		}
		
		if (data.get("z") != null) {
			condition.checkZ = true;
			condition.z = data.get("z").getAsInt();
		}
		
		if (data.get("radius") != null) {
			condition.radius = data.get("radius").getAsInt();
		}
		
		if (data.get("greaterThan") != null) {
			condition.greaterThan = data.get("greaterThan").getAsBoolean();
		}
		
		if (data.get("lessThan") != null) {
			condition.lessThan = data.get("lessThan").getAsBoolean();
		}
		
		return condition;
	}

	@Override
	public void serialize(JsonObject elements) {
		if (checkDimension) 
			elements.addProperty("dimension", dimension);
		if (checkX)
			elements.addProperty("x", x);
		if (checkY)
			elements.addProperty("y", y);
		if (checkZ)
			elements.addProperty("z", z);
		if (radius > 0)
			elements.addProperty("radius", radius);
		if (greaterThan != false)
			elements.addProperty("greaterThan", greaterThan);
		if (lessThan != false)
			elements.addProperty("lessThan", lessThan);
	}
}
