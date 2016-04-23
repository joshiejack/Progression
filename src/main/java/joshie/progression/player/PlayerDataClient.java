package joshie.progression.player;

import joshie.progression.criteria.Criteria;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.network.PacketCompleted.DummyAchievement;
import joshie.progression.player.data.AbilityStats;
import joshie.progression.player.data.CustomStats;
import joshie.progression.player.data.Points;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.achievement.GuiAchievement;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
        if (this.team != null && this.team.getOwner() != team.getOwner()) {
            GuiAchievement gui = Minecraft.getMinecraft().guiAchievement;
            gui.displayUnformattedAchievement(new DummyAchievement(new Criteria(null, null) {
                @Override
                public ItemStack getIcon() {
                    return new ItemStack(Items.diamond_pickaxe);
                }
            }) {
                @Override
                @SideOnly(Side.CLIENT)
                public String getDescription() {
                    return "Joined " + PlayerTracker.getClientPlayer().getTeam().getName();
                }
            });
            ReflectionHelper.setPrivateValue(GuiAchievement.class, gui, Minecraft.getSystemTime(), "notificationTime", "field_146263_l");
            ReflectionHelper.setPrivateValue(GuiAchievement.class, gui, false, "permanentNotification", "field_146262_n");
        }

        super.setTeam(team);
        GROUP_EDITOR.clear();
    }
}
