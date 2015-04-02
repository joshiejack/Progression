package joshie.crafting.conditions;

import java.util.UUID;

import joshie.crafting.api.ICondition;
import joshie.crafting.gui.TextFieldHelper.IntegerFieldHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.Event.Result;

public class ConditionCoordinates extends ConditionBase {
    private IntegerFieldHelper radiusEdit;
    private IntegerFieldHelper dimensionEdit;
    private IntegerFieldHelper xEdit;
    private IntegerFieldHelper yEdit;
    private IntegerFieldHelper zEdit;
    
    public boolean checkDimension = false; //Whether we check the dimension
	public boolean checkX = true; //Whether we check the x coordinate
	public boolean checkY = true; //Whether we check the y coordinate
	public boolean checkZ = true; //Whether we check the x coordinate
	public int radius = 0; //How much further to check than the block
	public int dimension = 0, x = 0, y = 0, z = 0; //The coordinates
	public boolean greaterThan = false; //If we are checking greater than
	public boolean lessThan = false; //If we are checking less than
	
	public ConditionCoordinates() {
		super("Coordinates", 0xFF000000, "coordinates");
		radiusEdit = new IntegerFieldHelper("radius", this);
		dimensionEdit = new IntegerFieldHelper("dimension", this);
		xEdit = new IntegerFieldHelper("x", this);
		yEdit = new IntegerFieldHelper("y", this);
		zEdit = new IntegerFieldHelper("z", this);
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
		
		condition.checkX = false;
		condition.checkY = false;
		condition.checkZ = false;
		
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

    @Override
    public ICondition newInstance() {
        return new ConditionCoordinates();
    }
    
    @Override
    public Result clicked() {
        if (mouseX <= 94 && mouseX >= 1) {
            if (mouseY > 25 && mouseY <= 33) checkDimension = !checkDimension;
            if (mouseY > 34 && mouseY <= 41) checkX = !checkX;
            if (mouseY > 41 && mouseY <= 48) checkY = !checkY;
            if (mouseY > 48 && mouseY <= 55) checkZ = !checkZ;
            if (mouseY > 55 && mouseY <= 62) radiusEdit.select();
            if (mouseY > 62 && mouseY <= 69) dimensionEdit.select();
            if (mouseY > 69 && mouseY <= 76) xEdit.select();
            if (mouseY > 76 && mouseY <= 83) yEdit.select();
            if (mouseY > 83 && mouseY <= 90) zEdit.select();
            if (mouseY > 90 && mouseY <= 97) greaterThan = !greaterThan;
            if (mouseY > 97 && mouseY <= 104) lessThan = !lessThan;
            if (mouseY >= 17 && mouseY < 100) return Result.ALLOW;
        }

        return Result.DEFAULT;
    }

    @Override
    public void draw() {
        int checkDColor = 0xFFFFFFFF;
        int checkXColor = 0xFFFFFFFF;
        int checkYColor = 0xFFFFFFFF;
        int checkZColor = 0xFFFFFFFF;
        int radiusColor = 0xFFFFFFFF;
        int dimColor = 0xFFFFFFFF;
        int xColor = 0xFFFFFFFF;
        int yColor = 0xFFFFFFFF;
        int zColor = 0xFFFFFFFF;
        int gColor = 0xFFFFFFFF;
        int lColor = 0xFFFFFFFF;
        
        int amountColor = 0xFF000000;
        int match2Color = 0xFF000000;
        if (mouseX <= 94 && mouseX >= 1) {
            if (mouseY > 25 && mouseY <= 33) checkDColor = 0xFFBBBBBB;
            if (mouseY > 34 && mouseY <= 41) checkXColor = 0xFFBBBBBB;
            if (mouseY > 41 && mouseY <= 48) checkYColor = 0xFFBBBBBB;
            if (mouseY > 48 && mouseY <= 55) checkZColor = 0xFFBBBBBB;
            if (mouseY > 55 && mouseY <= 62) radiusColor = 0xFFBBBBBB;
            if (mouseY > 62 && mouseY <= 69) dimColor = 0xFFBBBBBB;
            if (mouseY > 69 && mouseY <= 76) xColor = 0xFFBBBBBB;
            if (mouseY > 76 && mouseY <= 83) yColor = 0xFFBBBBBB;
            if (mouseY > 83 && mouseY <= 90) zColor = 0xFFBBBBBB;
            if (mouseY > 90 && mouseY <= 97) gColor = 0xFFBBBBBB;
            if (mouseY > 97 && mouseY <= 104) lColor = 0xFFBBBBBB;
        }

        drawText("checkDim: " + checkDimension , 4, 25, checkDColor);
        drawText("checkX: " + checkX, 4, 34, checkXColor);
        drawText("checkY: " + checkY, 4, 41, checkYColor);
        drawText("checkZ: " + checkZ, 4, 48, checkZColor);
        drawText("radius: " + radiusEdit, 4, 55, radiusColor);
        drawText("dimension: " + dimensionEdit, 4, 62, dimColor);
        drawText("x: " + xEdit, 4, 69, xColor);
        drawText("y: " + yEdit, 4, 76, yColor);
        drawText("z: " + zEdit, 4, 83, zColor);
        drawText("greaterThan: " + greaterThan, 4, 90, gColor);
        drawText("lessThan: " + lessThan, 4, 97, lColor);
    }
}
