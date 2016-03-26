package joshie.progression.plugins.enchiridion.rewards;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.progression.criteria.rewards.RewardBase;
import joshie.progression.helpers.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

public class RewardOpenBook extends RewardBase {
    public String bookid = "";
    public int page = 1;

    public RewardOpenBook() {
        super("open.book", 0xFFCCCCCC);
    }

    @Override
    public void reward(UUID uuid) {
        EntityPlayer player = PlayerHelper.getPlayerFromUUID(uuid);
        if (player != null) {
            EnchiridionAPI.instance.openBook(player, bookid, page);
        }
    }
}
