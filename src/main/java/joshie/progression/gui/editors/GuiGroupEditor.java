package joshie.progression.gui.editors;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mojang.authlib.GameProfile;

import joshie.progression.gui.core.FeatureBarsFull;
import joshie.progression.gui.core.FeatureTooltip;
import joshie.progression.gui.core.GuiCore;
import joshie.progression.gui.core.IBarProvider;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.player.PlayerTeam;
import joshie.progression.player.PlayerTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.UsernameCache;

public class GuiGroupEditor extends GuiBaseEditor implements IBarProvider {
    public static final GuiGroupEditor INSTANCE = new GuiGroupEditor();
    private static Cache<PlayerTeam, Set<AbstractClientPlayer>> playerList;

    @Override
    public IEditorMode getPreviousGui() {
        return null;
    }
    
    @Override
    public Object getKey() {
        return this;
    }
    
    public void clear() {
        playerList = CacheBuilder.newBuilder().maximumSize(64).build(); //Reset the cache when we reload the screen
    }

    @Override
    public void initData(GuiCore core) {
        super.initData(core);
        core.scrollingEnabled = false;
        features.add(new FeatureBarsFull(this, "group"));
    }

    @Override
    public void drawGuiForeground(boolean overlayvisible, int mouseX, int mouseY) {
        drawGradientRectWithBorder(2, 25, (screenWidth / 2) - 2, 25 + 15, 0xFFCCCCCC, theme.conditionEditorGradient1, theme.conditionEditorBorder);
        drawText("Team Editor", 10, 29, 0xFFFFFFFF);

        drawGradientRectWithBorder((screenWidth / 2) + 2, 25, screenWidth - 2, 25 + 15, 0xFFCCCCCC, theme.conditionEditorGradient1, theme.conditionEditorBorder);
        String text = "Team Selector";
        drawText(text, (int) (screenWidth - (text.length() * 6.5)), 29, 0xFFFFFFFF);

        PlayerTeam team = PlayerTracker.getClientPlayer().getTeam();
        drawText("Team Type:", 5, 45, 0xFFFFFFFF);
        drawText(team.getType().name(), 85, 45, 0xFFFFFFFF);
        drawText("Team Name:", 5, 55, 0xFFFFFFFF);
        drawText(TextEditor.INSTANCE.getText(team), 85, 55, 0xFFFFFFFF);
        if (mouseX >= 85 && mouseX <= (screenWidth / 2) - 2) {
            if (mouseY >= 55 && mouseY < 65) {
                if (team.isOwner(MCClientHelper.getPlayer())) {
                    FeatureTooltip.INSTANCE.addTooltip(EnumChatFormatting.BOLD + "Click to edit name");
                } else FeatureTooltip.INSTANCE.addTooltip(EnumChatFormatting.BOLD + "Only owner can rename the team");
            }
        }

        drawText(EnumChatFormatting.UNDERLINE + "Team Members", 5, 65, 0xFFFFFFFF);
        try {
            int xPos = 0;
            for (EntityPlayer player : getPlayers(team)) {
                GuiInventory.drawEntityOnScreen(20 + xPos, 120 + screenTop, 20, 5, 10, player);
                xPos += 30;
            }
        } catch (Exception e) {}

        drawGradientRectWithBorder((screenWidth / 2) + 2, 45, screenWidth - 2, 85, 0xFFCCCCCC, theme.conditionEditorGradient1, 0xFF000000);
        text = "Create New Team";
        drawText(text, (screenWidth / 2) + 12, 59, 0xFFFFFFFF);

        drawGradientRectWithBorder((screenWidth / 2) + 2, 90, screenWidth - 2, 105, 0xFFCCCCCC, theme.conditionEditorGradient1, 0xFF000000);
        text = "Current Invites";
        drawText(text, (screenWidth / 2) + 12, 59, 0xFFFFFFFF);
    }

    public Set<AbstractClientPlayer> getPlayers(final PlayerTeam team) {
        try {
            return playerList.get(team, new Callable<Set<AbstractClientPlayer>>() {
                @Override
                public Set<AbstractClientPlayer> call() throws Exception {
                    Set<AbstractClientPlayer> players = new LinkedHashSet();
                    Minecraft mc = Minecraft.getMinecraft();
                    players.add(new EntityOtherPlayerMP(mc.theWorld, new GameProfile(team.getOwner(), UsernameCache.getLastKnownUsername(team.getOwner()))));
                    if (!team.isOwner(mc.thePlayer)) players.add(mc.thePlayer);
                    for (UUID uuid : team.getMembers()) {
                        if (uuid.equals(PlayerHelper.getUUIDForPlayer(mc.thePlayer))) continue;
                        else {
                            players.add(new EntityOtherPlayerMP(mc.theWorld, new GameProfile(uuid, UsernameCache.getLastKnownUsername(uuid))));
                        }
                    }

                    return players;
                }
            });
        } catch (Exception e) {
            return new LinkedHashSet();
        }
    }

    @Override
    public boolean guiMouseClicked(boolean overlayvisible, int mouseX, int mouseY, int button) {
        PlayerTeam team = PlayerTracker.getClientPlayer().getTeam();
        if (team.isOwner(MCClientHelper.getPlayer())) {
            if (mouseX >= 85 && mouseX <= (screenWidth / 2) - 2) {
                if (mouseY >= 55 && mouseY < 65) {
                    TextEditor.INSTANCE.setEditable(team);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public int getColorForBar(BarColorType type) {
        switch (type) {
            case BAR1_GRADIENT1:
                return theme.conditionEditorGradient1;
            case BAR1_GRADIENT2:
                return theme.conditionEditorGradient2;
            case BAR1_BORDER:
                return theme.conditionEditorUnderline2;
            case BAR1_FONT:
                return theme.conditionEditorFont;
            case BAR1_UNDERLINE:
                return theme.conditionEditorUnderline;
            default:
                return 0;
        }
    }
}
