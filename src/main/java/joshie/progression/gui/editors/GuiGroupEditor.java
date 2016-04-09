package joshie.progression.gui.editors;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mojang.authlib.GameProfile;
import joshie.progression.gui.buttons.ButtonLeaveTeam;
import joshie.progression.gui.buttons.ButtonNewTeam;
import joshie.progression.gui.core.FeatureBarsFull;
import joshie.progression.gui.core.GuiCore;
import joshie.progression.gui.core.IBarProvider;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.lib.ProgressionInfo;
import joshie.progression.player.PlayerTeam;
import joshie.progression.player.PlayerTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.UsernameCache;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;

import static net.minecraft.util.EnumChatFormatting.BOLD;
import static net.minecraft.util.EnumChatFormatting.ITALIC;

public class GuiGroupEditor extends GuiBaseEditor implements IBarProvider, ITextEditable {
    public static final GuiGroupEditor INSTANCE = new GuiGroupEditor();
    private static Cache<PlayerTeam, Set<AbstractClientPlayer>> playerList;
    private boolean isPopup;
    private String username;

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
        List<GuiButton> buttons = core.getButtonNewList();
        buttons.add(new ButtonNewTeam("New Team", 5, core.screenTop + 25));
        buttons.add(new ButtonLeaveTeam("Leave Team", 75, core.screenTop + 25));
        features.add(new FeatureBarsFull(this, "group"));
    }

    private boolean clickLeft(int screenCentre, int mouseX, int mouseY) {
        PlayerTeam team = PlayerTracker.getClientPlayer().getTeam();
        if (!isPopup && mouseY >= 90 && mouseY < 100) {
            return TextEditor.INSTANCE.setEditable(team);
        }

        if (!team.isSingle() && team.isOwner(MCClientHelper.getPlayer())) {
            int xPos = getPlayers(team).size() * 40;
            if (mouseX >= 10 + xPos && mouseX <= 65 + xPos && mouseY >= 170 && mouseY <= 225) {
                username = ""; //Reset the username everytime you click this
                if (isPopup) {
                    //If we had the popup and clicked this button
                } else TextEditor.INSTANCE.setEditable(this);

                isPopup = !isPopup;
                return true;
            } else {
                TextEditor.INSTANCE.clearEditable();
                isPopup = false;
            }
        }

        return false;
    }

    private void drawLeft(int screenCentre, int mouseX, int mouseY) {
        drawGradientRectWithBorder(-2, 50, core.screenWidth + 4, 50 + 15, 0xFF6C00D9, 0xFF330066, 0xFF330066);
        drawText("Team Info", 10, 54, 0xFFFFFFFF);

        PlayerTeam team = PlayerTracker.getClientPlayer().getTeam();
        drawText("Team Type:", 5, 70, 0xFFFFFFFF);
        drawText(team.getType().name(), 105, 70, 0xFFFFFFFF);

        drawText("Team Owner:", 5, 80, 0xFFFFFFFF);
        drawText(UsernameCache.getLastKnownUsername(team.getOwner()), 105, 80, 0xFFFFFFFF);

        drawText("Team Name:", 5, 90, 0xFFFFFFFF);
        drawText(TextEditor.INSTANCE.getText(team), 105, 90, 0xFFFFFFFF);

        if (isPopup) {
            core.drawRectWithBorder(100, 86, 300, 86 + 15, 0xFF000000, 0xFFFFFFFF);
            String display = username.equals("") ? "Enter a Username..." : TextEditor.INSTANCE.getText(this);
            drawText(display, 110, 90, 0xFFFFFFFF);
        }

        if (!team.isSingle()) {
            drawText("Multiple Rewards:", 5, 100, 0xFFFFFFFF);
            drawText("" + team.giveMultipleRewards(), 105, 100, 0xFFFFFFFF);
            if (!isPopup && mouseY >= 100 && mouseY < 110) {
                addTooltip(BOLD + "Multiple Rewards");
                addTooltip("If this is true, team members will be able to claim their own rewards where applicable\n\n" + ITALIC + "Click to Toggle", 45);
            }

            drawText("True Team:", 5, 110, 0xFFFFFFFF);
            drawText("" + team.isTrueTeam(), 105, 110, 0xFFFFFFFF);
            if (!isPopup && mouseY >= 110 && mouseY < 120) {
                addTooltip(BOLD + "True Team");
                addTooltip("If this is true, then tasks that require you to have items in your inventory will count the items from all the team members instead of checking for them on and individual basis\n\n" + ITALIC + "Click to Toggle", 50);
            }
        }

        if (!isPopup && mouseY >= 90 && mouseY < 100) {
            if (team.isOwner(MCClientHelper.getPlayer())) {
                addTooltip(BOLD + "Click to edit name");
            } else addTooltip(BOLD + "Only owner can rename the team");
        }

        drawGradientRectWithBorder(-2, 141, core.screenWidth + 4, 141 + 15, 0xFF6C00D9, 0xFF330066, 0xFF330066);
        drawText("Team Members", 10, 145, 0xFFFFFFFF);
        try {
            int xPos = 0;
            for (EntityPlayer player : getPlayers(team)) {
                GuiInventory.drawEntityOnScreen(20 + xPos, 230 + core.screenTop, 35, 5, 10, player);
                if (mouseX >= xPos && mouseX <= xPos + 39 && mouseY >= 164 && mouseY <= 231) {
                    addTooltip(player.getDisplayNameString());
                }

                xPos += 40;
            }

            if (!team.isSingle() && team.isOwner(MCClientHelper.getPlayer())) {
                int crossY = 64;
                if (mouseX >= 10 + xPos && mouseX <= 65 + xPos && mouseY >= 170 && mouseY <= 225) {
                    addTooltip(BOLD + "Add New Member");
                    addTooltip("Click this to open a window to invite a new member, once the window is open and you have typed who you want to invite, press enter to send the invite", 40);
                    crossY = 119;
                }

                GlStateManager.enableBlend();
                int color = 0xFF6C00D9;
                float red = (color >> 16 & 255) / 255.0F;
                float green = (color >> 8 & 255) / 255.0F;
                float blue = (color & 255) / 255.0F;
                GlStateManager.color(red, green, blue, 1F);
                core.drawTexture(ProgressionInfo.textures, xPos + 10, 170, 201, crossY, 55, 55);
            }
        } catch (Exception e) {}
    }

    private boolean clickRight(int screenCentre, int mouseX, int mouseY) {
        return false;
    }

    private void drawRight(int screenCentre, int mouseX, int mouseY) {
        if (isPopup) {

        }

        //drawGradientRectWithBorder(screenCentre + 2, 90, core.screenWidth - 2, 105, 0xFFCCCCCC, theme.conditionEditorGradient1, 0xFF000000);
        //text = "Current Invites";
        //drawText(text, screenCentre + 12, 59, 0xFFFFFFFF);
    }

    @Override
    public void drawGuiForeground(boolean overlayvisible, int mouseX, int mouseY) {
        int screenCentre = core.screenWidth / 2;
        drawLeft(screenCentre, mouseX, mouseY);
        drawRight(screenCentre, mouseX, mouseY);
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
        int screenCentre = core.screenWidth / 2;
        PlayerTeam team = PlayerTracker.getClientPlayer().getTeam();
        if (team.isOwner(MCClientHelper.getPlayer()) && clickLeft(screenCentre, mouseX, mouseY)) return true;
        else if (clickRight(screenCentre, mouseX, mouseY)) return true;
        else return false;
    }

    @Override
    public int getColorForBar(BarColorType type) {
        switch (type) {
            case BAR1_GRADIENT1:
                return 0xFF6C00D9;
            case BAR1_GRADIENT2:
                return 0xFF330066;
            case BAR1_BORDER:
                return 0xFF330066;
            case BAR1_FONT:
                return theme.conditionEditorFont;
            case BAR1_UNDERLINE:
                return 0x00330066;
            default:
                return 0;
        }
    }

    @Override
    public String getTextField() {
        return username;
    }

    @Override
    public void setTextField(String text) {
        username = text;
    }
}
