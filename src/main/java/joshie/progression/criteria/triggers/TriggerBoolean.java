package joshie.progression.criteria.triggers;

import java.util.UUID;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IProgressionTriggerData;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class TriggerBoolean extends TriggerBaseBoolean {
    public String variable = "default";
    public boolean isTrue = true;

    public TriggerBoolean() {
        super(new ItemStack(Items.redstone), "boolean", 0xFF26C9FF);
    }

    @Override
    public boolean onFired(UUID uuid, IProgressionTriggerData iTriggerData, Object... data) {
        boolean check = ProgressionAPI.player.getBoolean(uuid, variable);
        if (check == isTrue) {
            markTrue(iTriggerData);
        }

        return true;
    }
}
