package joshie.crafting.conditions;

import java.util.UUID;

import joshie.crafting.api.DrawHelper;
import joshie.crafting.gui.TextFieldHelper.IntegerFieldHelper;
import joshie.crafting.helpers.JSONHelper;
import joshie.crafting.json.Theme;
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
        super("coordinates", 0xFF000000);
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
        boolean xMatches = checkX ? false : true;
        boolean yMatches = checkY ? false : true;
        boolean zMatches = checkZ ? false : true;
        if (greaterThan || lessThan) {
            if (greaterThan) {
                if (playerX >= x) xMatches = true;
                if (playerY >= y) yMatches = true;
                if (playerZ >= z) zMatches = true;
            } else {
                if (playerX <= x) xMatches = true;
                if (playerY <= y) yMatches = true;
                if (playerZ <= z) zMatches = true;
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
    public void readFromJSON(JsonObject data) {
        checkDimension = false;
        checkX = false;
        checkY = false;
        checkZ = false;

        if (JSONHelper.getExists(data, "dimension")) {
            checkDimension = true;
            dimension = JSONHelper.getInteger(data, "dimension", dimension);
        }

        if (JSONHelper.getExists(data, "x")) {
            checkX = true;
            x = JSONHelper.getInteger(data, "x", x);
        }

        if (JSONHelper.getExists(data, "y")) {
            checkY = true;
            y = JSONHelper.getInteger(data, "y", y);
        }

        if (JSONHelper.getExists(data, "z")) {
            checkZ = true;
            z = JSONHelper.getInteger(data, "z", z);
        }

        greaterThan = JSONHelper.getBoolean(data, "greaterThan", greaterThan);
        lessThan = JSONHelper.getBoolean(data, "lessThan", lessThan);
    }

    @Override
    public void writeToJSON(JsonObject data) {
        if (checkDimension) JSONHelper.setInteger(data, "dimension", dimension, 0);
        if (checkX) JSONHelper.setInteger(data, "x", x, 0);
        if (checkY) JSONHelper.setInteger(data, "y", y, 0);
        if (checkZ) JSONHelper.setInteger(data, "z", z, 0);
        JSONHelper.setInteger(data, "radius", radius, 0);
        JSONHelper.setBoolean(data, "greaterThan", greaterThan, false);
        JSONHelper.setBoolean(data, "lessThan", lessThan, false);
    }

    @Override
    public Result onClicked(int mouseX, int mouseY) {
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
    public void draw(int mouseX, int mouseY) {
        int checkDColor = Theme.INSTANCE.optionsFontColor;
        int checkXColor = Theme.INSTANCE.optionsFontColor;
        int checkYColor = Theme.INSTANCE.optionsFontColor;
        int checkZColor = Theme.INSTANCE.optionsFontColor;
        int radiusColor = Theme.INSTANCE.optionsFontColor;
        int dimColor = Theme.INSTANCE.optionsFontColor;
        int xColor = Theme.INSTANCE.optionsFontColor;
        int yColor = Theme.INSTANCE.optionsFontColor;
        int zColor = Theme.INSTANCE.optionsFontColor;
        int gColor = Theme.INSTANCE.optionsFontColor;
        int lColor = Theme.INSTANCE.optionsFontColor;

        if (mouseX <= 94 && mouseX >= 1) {
            if (mouseY > 25 && mouseY <= 33) checkDColor = Theme.INSTANCE.optionsFontColorHover;
            if (mouseY > 34 && mouseY <= 41) checkXColor = Theme.INSTANCE.optionsFontColorHover;
            if (mouseY > 41 && mouseY <= 48) checkYColor = Theme.INSTANCE.optionsFontColorHover;
            if (mouseY > 48 && mouseY <= 55) checkZColor = Theme.INSTANCE.optionsFontColorHover;
            if (mouseY > 55 && mouseY <= 62) radiusColor = Theme.INSTANCE.optionsFontColorHover;
            if (mouseY > 62 && mouseY <= 69) dimColor = Theme.INSTANCE.optionsFontColorHover;
            if (mouseY > 69 && mouseY <= 76) xColor = Theme.INSTANCE.optionsFontColorHover;
            if (mouseY > 76 && mouseY <= 83) yColor = Theme.INSTANCE.optionsFontColorHover;
            if (mouseY > 83 && mouseY <= 90) zColor = Theme.INSTANCE.optionsFontColorHover;
            if (mouseY > 90 && mouseY <= 97) gColor = Theme.INSTANCE.optionsFontColorHover;
            if (mouseY > 97 && mouseY <= 104) lColor = Theme.INSTANCE.optionsFontColorHover;
        }

        DrawHelper.drawText("checkDim: " + checkDimension, 4, 25, checkDColor);
        DrawHelper.drawText("checkX: " + checkX, 4, 34, checkXColor);
        DrawHelper.drawText("checkY: " + checkY, 4, 41, checkYColor);
        DrawHelper.drawText("checkZ: " + checkZ, 4, 48, checkZColor);
        DrawHelper.drawText("radius: " + radiusEdit, 4, 55, radiusColor);
        DrawHelper.drawText("dimension: " + dimensionEdit, 4, 62, dimColor);
        DrawHelper.drawText("x: " + xEdit, 4, 69, xColor);
        DrawHelper.drawText("y: " + yEdit, 4, 76, yColor);
        DrawHelper.drawText("z: " + zEdit, 4, 83, zColor);
        DrawHelper.drawText("greaterThan: " + greaterThan, 4, 90, gColor);
        DrawHelper.drawText("lessThan: " + lessThan, 4, 97, lColor);
    }
}
