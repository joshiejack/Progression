package joshie.progression.criteria.rewards;

import java.util.List;
import java.util.UUID;

import joshie.progression.api.IItemFilter;
import joshie.progression.gui.fields.ItemFilterFieldPreview;
import joshie.progression.helpers.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class RewardClear extends RewardBaseItemFilter {
    public int toTake = 1;
    
    public RewardClear() {
        super("clear", 0xFF69008C);
        //list.add(new TextField("toTake", this));
        list.add(new ItemFilterFieldPreview("filters", this, 50, 40, 10, 100, 30, 100, 2F));
    }

    @Override
    public void reward(UUID uuid) {
        int taken = 0;
        
        List<EntityPlayerMP> players = PlayerHelper.getPlayersFromUUID(uuid);
        for (int k = 0; k < players.size() && taken < toTake; k++) {
            EntityPlayer player = players.get(k);
            if (player != null) {
                for (int i = 0; i < player.inventory.mainInventory.length && taken < toTake; i++) {
                    ItemStack check = player.inventory.mainInventory[i];
                    int decrease = 0;

                    if (check != null) {
                        for (int j = 0; j < check.stackSize && taken < toTake; j++) {
                            for (IItemFilter filter: filters) {
                                if (filter.matches(check)) {
                                    decrease++;
                                    taken++;
                                }
                            }
                        }

                        if (decrease > 0) player.inventory.decrStackSize(i, decrease);
                    }
                }
            }
        }
    }

    @Override
    public void addTooltip(List list) {
        list.add(EnumChatFormatting.WHITE + "Remove Item");
        ItemStack stack = preview == null ? BROKEN : preview;
        list.add(stack.getDisplayName() + " x" + stack.stackSize);
    }
}
