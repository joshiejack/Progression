package joshie.progression.criteria.rewards;

import java.util.List;
import java.util.UUID;

import joshie.progression.api.DrawHelper;
import joshie.progression.gui.SelectTextEdit;
import joshie.progression.gui.SelectTextEdit.ITextEditable;
import joshie.progression.helpers.ClientHelper;
import joshie.progression.helpers.JSONHelper;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.json.Theme;
import joshie.progression.lib.FakeOp;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.Event.Result;

public class RewardCommand extends RewardBase implements ITextEditable {
    private String command = "dummy";

    public RewardCommand() {
        super("command", 0xFF2626FF);
    }

    @Override
    public void readFromJSON(JsonObject data) {
        command = JSONHelper.getString(data, "command", command);
    }

    @Override
    public void writeToJSON(JsonObject data) {
        JSONHelper.setString(data, "command", command, "dummy");
    }

    @Override
    public void reward(UUID uuid) {
        EntityPlayer player = PlayerHelper.getPlayerFromUUID(uuid);
        if (player != null) {
            String newCommand = command.replace("@u", player.getCommandSenderName());
            MinecraftServer.getServer().getCommandManager().executeCommand(FakeOp.getInstance(), newCommand);
        }
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Blocks.command_block);
    }

    @Override
    public Result onClicked(int mouseX, int mouseY) {
        if (mouseX <= 84 && mouseX >= 1) {
            if (mouseY >= 17 && mouseY <= 100) {
                SelectTextEdit.INSTANCE.select(this);
                return Result.ALLOW;
            }
        }

        return Result.DEFAULT;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        int commandColor = Theme.INSTANCE.optionsFontColor;
        if (ClientHelper.canEdit()) {
            if (mouseX <= 84 && mouseX >= 1) {
                if (mouseY >= 17 && mouseY <= 100) commandColor = Theme.INSTANCE.optionsFontColorHover;
            }
        }

        DrawHelper.drawText("command: ", 4, 18, commandColor);
        DrawHelper.drawSplitText(SelectTextEdit.INSTANCE.getText(this), 4, 26, 200, commandColor);
    }

    @Override
    public String getTextField() {
        return command;
    }

    @Override
    public void setTextField(String text) {
        this.command = text;
    }

    @Override
    public void addTooltip(List list) {
        list.add(EnumChatFormatting.WHITE + "Execute Command");
        list.add(command);
    }
}
