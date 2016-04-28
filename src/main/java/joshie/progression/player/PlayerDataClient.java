package joshie.progression.player;

import joshie.progression.helpers.AchievementHelper;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.player.data.AbilityStats;
import joshie.progression.player.data.CustomStats;
import joshie.progression.player.data.Points;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.UUID;

import static joshie.progression.gui.core.GuiList.GROUP_EDITOR;

public class PlayerDataClient extends PlayerDataCommon {
    private static PlayerDataClient INSTANCE = new PlayerDataClient();

    public static PlayerDataClient getInstance() {
        return INSTANCE;
    }

    public UUID getUUID() {
        return PlayerHelper.getUUIDForPlayer(MCClientHelper.getPlayer());
    }

    public void setAbilities(AbilityStats abilities) {
        this.abilities = abilities;
    }

    public void setCustomData(CustomStats data) {
        this.custom = data;
    }

    public void setPoints(Points points) {
        this.points = points;
    }

    @Override
    public void setTeam(PlayerTeam team) {
        if (this.team != null && this.team.getOwner() != team.getOwner() && this.team != team) {
            AchievementHelper.display(new ItemStack(Items.banner), "Joined " + PlayerTracker.getClientPlayer().getTeam().getName());
        }

        super.setTeam(team);
        GROUP_EDITOR.clear();
    }
}
