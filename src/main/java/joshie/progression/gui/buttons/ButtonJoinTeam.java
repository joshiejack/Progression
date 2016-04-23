package joshie.progression.gui.buttons;

import joshie.progression.gui.editors.GuiGroupEditor.Invite;
import joshie.progression.helpers.SplitHelper;
import joshie.progression.network.PacketChangeTeam;
import joshie.progression.network.PacketHandler;
import net.minecraft.client.gui.GuiScreen;

import static joshie.progression.gui.core.GuiList.GROUP_EDITOR;
import static joshie.progression.gui.core.GuiList.TOOLTIP;
import static joshie.progression.player.PlayerSavedData.TeamAction.JOIN;
import static net.minecraft.util.EnumChatFormatting.BOLD;

public class ButtonJoinTeam extends ButtonBaseTeam {
    private Invite invite;

    public ButtonJoinTeam(Invite invite, int x, int y) {
        super("Join " + invite.name, x, y);
        this.invite = invite;
    }

    @Override
    public void onClicked() {
        if (!GuiScreen.isShiftKeyDown()) PacketHandler.sendToServer(new PacketChangeTeam(JOIN, invite.owner));

        GROUP_EDITOR.removeInvite(invite); //Remove the invite always
    }

    @Override
    public void addTooltip() {
        TOOLTIP.add(BOLD + "Join Team");
        TOOLTIP.add(SplitHelper.splitTooltip("If you want to join this team, click, if not shift click.", 40));
    }
}
