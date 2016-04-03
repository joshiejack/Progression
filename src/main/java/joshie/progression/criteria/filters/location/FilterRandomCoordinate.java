package joshie.progression.criteria.filters.location;

import joshie.progression.Progression;
import joshie.progression.api.special.IEnum;
import joshie.progression.lib.WorldLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

import static joshie.progression.criteria.filters.location.FilterLocationBase.LocationOperator.*;

public abstract class FilterRandomCoordinate extends FilterLocationBase implements IEnum {
    public LocationOperator operator = LocationOperator.RADIUS;
    public int coordinate = 8;
    public int distance = 32;
    public String coordstring;

    public FilterRandomCoordinate(String name) {
        super(name, 0xFFBBBBBB);
        this.coordstring = name;
    }

    public boolean isInCircle(int coord) {
        for(int i = -distance; i <= distance; i++) {
            for (int k = -distance; k <= distance; k++) {
                int position = i * i + k * k;
                if (position >= (distance + 0.50f) * (distance + 0.50f)) {
                    continue; //Outside of the circle?
                }

                if (coord >= -position && coord <= +position) return true;
            }
        }

        return false;
    }

    @Override
    public WorldLocation getRandom(EntityPlayer player) {
        int random = 0;
        if (operator == THISORMORE) random = distance >= 1 ? player.worldObj.rand.nextInt(distance): random;
        else if (operator == THISORLESS) random = distance >= 1 ? - player.worldObj.rand.nextInt(distance): random;
        else if (operator == RADIUS) random = distance >= 1 ? player.worldObj.rand.nextInt(distance * 2) - distance : 0;
        return new WorldLocation(player.dimension, getLocation(player, random));
    }

    public abstract BlockPos getLocation(EntityPlayer player, int random);

    @Override
    public String getDescription() {
        return Progression.format("filter.location." + coordstring + "description", coordinate);
    }

    public abstract int getCoordinate(WorldLocation location);

    @Override
    public boolean matches(WorldLocation location) {
        int check = getCoordinate(location);
        if (operator == THISORMORE) return check >= coordinate - distance;
        else if (operator == THISORLESS) return check <= coordinate + distance;
        else if (operator == RADIUS) {
            if (check < coordinate - distance || check > coordinate + distance) return false;
            return true; //Now we return true as we aren't outside the defined boundary
        }

        return false;
    }

    @Override
    public Enum next(String name) {
        int id = operator.ordinal() + 1;
        if (id < LocationOperator.values().length) {
            return LocationOperator.values()[id];
        }

        return LocationOperator.values()[0];
    }

    @Override
    public boolean isEnum(String name) {
        return name.equals("operator");
    }
}
