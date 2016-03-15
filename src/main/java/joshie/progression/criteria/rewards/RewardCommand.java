package joshie.progression.criteria.rewards;

import java.util.List;
import java.util.UUID;

import joshie.progression.gui.fields.TextField;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.lib.FakeOp;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;

public class RewardCommand extends RewardBase {
    public String command = "dummy";

    public RewardCommand() {
        super("command", 0xFF2626FF);
        //list.add(new TextField("command", this));
    }

    @Override
    public void reward(UUID uuid) {
        EntityPlayer player = PlayerHelper.getPlayerFromUUID(uuid);
        if (player != null) {
            String newCommand = command.replace("@u", player.getDisplayNameString());
            MinecraftServer.getServer().getCommandManager().executeCommand(FakeOp.getInstance(), newCommand);
        }
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Blocks.command_block);
    }

    @Override
    public void addTooltip(List list) {
        list.add(EnumChatFormatting.WHITE + "Execute Command");
        list.add(command);
    }
}
