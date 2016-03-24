package joshie.progression.criteria.conditions;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.items.ItemCriteria;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.UUID;

public class ConditionBoolean extends ConditionBase {
    public String variable = "default";
    public boolean isTrue = true;

    public ConditionBoolean() {
        super(ItemCriteria.getStackFromMeta(ItemCriteria.ItemMeta.ifHasBoolean), "boolean", 0xFF00FFBF);
    }

    @Override
    public boolean isSatisfied(World world, EntityPlayer player, UUID uuid) {
        boolean check = ProgressionAPI.player.getBoolean(uuid, variable);
        if (check == isTrue) {
            return true;
        }

        return false;
    }
}
