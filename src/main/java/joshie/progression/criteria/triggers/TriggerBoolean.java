package joshie.progression.criteria.triggers;

import java.util.UUID;

import joshie.progression.api.ITriggerData;
import joshie.progression.player.PlayerTracker;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class TriggerBoolean extends TriggerBaseBoolean {
    public String variable = "default";
    public boolean isTrue = true;

    public TriggerBoolean() {
        super(new ItemStack(Items.redstone), "boolean", 0xFF26C9FF);
    }

    @Override
    public boolean onFired(UUID uuid, ITriggerData iTriggerData, Object... data) {
        double value = PlayerTracker.getServerPlayer(uuid).getAbilities().getPoints("boolean:" + variable);
        boolean check = value == 1D ? true : false;
        if (check == isTrue) {
            markTrue(iTriggerData);
        }

        return true;
    }
}
