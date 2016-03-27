package joshie.progression.criteria.rewards;

import joshie.progression.lib.FakeOp;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class RewardCommand extends RewardBase {
    public String command = "dummy";

    public RewardCommand() {
        super("command", 0xFF2626FF);
        //list.add(new TextField("command", this));
    }

    @Override
    public void reward(EntityPlayerMP player) {
        String newCommand = command.replace("@u", player.getDisplayNameString());
        MinecraftServer.getServer().getCommandManager().executeCommand(FakeOp.getInstance(), newCommand);
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
