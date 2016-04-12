package joshie.progression.gui.buttons;

import joshie.progression.helpers.SplitHelper;
import joshie.progression.network.PacketChangeTeam;
import joshie.progression.network.PacketHandler;

import static joshie.progression.gui.core.GuiList.TOOLTIP;
import static joshie.progression.player.PlayerSavedData.TeamAction.NEW;
import static net.minecraft.util.EnumChatFormatting.BOLD;

public class ButtonNewTeam extends ButtonBaseTeam {
    public ButtonNewTeam(String text, int x, int y) {
        super(text, x, y);
    }

    @Override
    public void onClicked() {
        PacketHandler.sendToServer(new PacketChangeTeam(NEW));
    }

    @Override
    public void addTooltip() {
        TOOLTIP.add(BOLD + "New Team");
        TOOLTIP.add(SplitHelper.splitTooltip("Clicking this button will create a new team, with you as the owner", 40));
    }
}
