package joshie.crafting.conditions;

import java.util.UUID;

import joshie.crafting.api.ICondition;
import joshie.crafting.plugins.minetweaker.Conditions;
import minetweaker.MineTweakerAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.google.gson.JsonObject;

@ZenClass("mods.craftcontrol.conditions.Coordinates")
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
	
	@ZenMethod
	public void add(String unique, int dimension, int x, int y, int z, boolean checkDimension, boolean checkX, boolean checkY, boolean checkZ, int radius, boolean greaterThan, boolean lessThan) {
		ConditionCoordinates condition = new ConditionCoordinates();
		condition.dimension = dimension;
		condition.x = x;
		condition.y = y;
		condition.z = z;
		condition.checkDimension = checkDimension;
		condition.checkX = checkX;
		condition.checkY = checkY;
		condition.checkZ = checkZ;
		condition.radius = radius;
		condition.greaterThan = true;
		condition.lessThan = lessThan;
		MineTweakerAPI.apply(new Conditions(unique, condition));
	}
	
	@ZenMethod
	public void addGreater(String unique, int x, int y, int z, boolean checkX, boolean checkY, boolean checkZ) {
		ConditionCoordinates condition = new ConditionCoordinates();
		condition.x = x;
		condition.y = y;
		condition.z = z;
		condition.checkX = checkX;
		condition.checkY = checkY;
		condition.checkZ = checkZ;
		condition.greaterThan = true;
		MineTweakerAPI.apply(new Conditions(unique, condition));
	}
	
	@ZenMethod
	public void addLess(String unique, int x, int y, int z, boolean checkX, boolean checkY, boolean checkZ) {
		ConditionCoordinates condition = new ConditionCoordinates();
		condition.x = x;
		condition.y = y;
		condition.z = z;
		condition.checkX = checkX;
		condition.checkY = checkY;
		condition.checkZ = checkZ;
		condition.lessThan = true;
		MineTweakerAPI.apply(new Conditions(unique, condition));
	}
	
	@ZenMethod
	public void addExact(String unique, int x, int y, int z, @Optional int radius) {
		ConditionCoordinates condition = new ConditionCoordinates();
		condition.x = x;
		condition.y = y;
		condition.z = z;
		MineTweakerAPI.apply(new Conditions(unique, condition));
	}
	
	@ZenMethod
	public void addDimension(String unique, int dimension) {
		ConditionCoordinates condition = new ConditionCoordinates();
		condition.checkDimension = true;
		condition.checkX = false;
		condition.checkY = false;
		condition.checkZ = false;
		condition.dimension = 0;
		MineTweakerAPI.apply(new Conditions(unique, condition));
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
		if (data.get("Dimension") != null) {
			condition.checkDimension = true;
			condition.dimension = data.get("Dimension").getAsInt();
		}
		
		if (data.get("X") != null) {
			condition.checkX = true;
			condition.x = data.get("X").getAsInt();
		}
		
		if (data.get("Y") != null) {
			condition.checkY = true;
			condition.y = data.get("Y").getAsInt();
		}
		
		if (data.get("Z") != null) {
			condition.checkZ = true;
			condition.z = data.get("Z").getAsInt();
		}
		
		if (data.get("Radius") != null) {
			condition.radius = data.get("Radius").getAsInt();
		}
		
		if (data.get("Greater Than") != null) {
			condition.greaterThan = data.get("Greater Than").getAsBoolean();
		}
		
		if (data.get("Less Than") != null) {
			condition.lessThan = data.get("Less Than").getAsBoolean();
		}
		
		return condition;
	}

	@Override
	public void serialize(JsonObject elements) {
		if (checkDimension) 
			elements.addProperty("Dimension", dimension);
		if (checkX)
			elements.addProperty("X", x);
		if (checkY)
			elements.addProperty("Y", y);
		if (checkZ)
			elements.addProperty("Z", z);
		if (radius > 0)
			elements.addProperty("Radius", radius);
		if (greaterThan != false)
			elements.addProperty("Greater Than", greaterThan);
		if (lessThan != false)
			elements.addProperty("Less Than", lessThan);
	}
}
