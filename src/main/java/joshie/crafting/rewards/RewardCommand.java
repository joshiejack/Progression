package joshie.crafting.rewards;

import java.util.List;
import java.util.UUID;

import joshie.crafting.api.IReward;
import joshie.crafting.gui.SelectTextEdit;
import joshie.crafting.gui.SelectTextEdit.ITextEditable;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.PlayerHelper;
import joshie.crafting.lib.FakeOp;
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
        super("Execute Command", 0xFF2626FF, "command");
    }

    @Override
    public IReward newInstance() {
        return new RewardCommand();
    }

    @Override
    public IReward deserialize(JsonObject data) {
        RewardCommand reward = new RewardCommand();
        reward.command = data.get("command").getAsString();
        return reward;
    }

    @Override
    public void serialize(JsonObject elements) {
        elements.addProperty("command", command);
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
    public Result clicked() {
        if (mouseX <= 84 && mouseX >= 1) {
            if (mouseY >= 17 && mouseY <= 100) {
                SelectTextEdit.INSTANCE.select(this);
                return Result.ALLOW;
            }
        }

        return Result.DEFAULT;
    }

    @Override
    public void draw() {
        int commandColor = 0xFFFFFFFF;
        if (ClientHelper.canEdit()) {
            if (mouseX <= 84 && mouseX >= 1) {
                if (mouseY >= 17 && mouseY <= 100) commandColor = 0xFFBBBBBB;
            }
        }

        drawText("command: ", 4, 18, commandColor);
        drawSplitText(SelectTextEdit.INSTANCE.getText(this), 4, 26, 200, commandColor);
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
