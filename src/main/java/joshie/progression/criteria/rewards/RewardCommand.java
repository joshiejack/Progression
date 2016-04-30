package joshie.progression.criteria.rewards;

import joshie.progression.Progression;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.ICustomDescription;
import joshie.progression.api.special.ICustomTooltip;
import joshie.progression.lib.FakeOp;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.List;

@ProgressionRule(name="command", color=0xFF2626FF, icon="minecraft:command_block")
public class RewardCommand extends RewardBase implements ICustomDescription, ICustomTooltip {
    public String command = "dummy";

    @Override
    public String getDescription() {
        return Progression.format(getProvider().getUnlocalisedName() + ".description", command);
    }

    @Override
    public void addTooltip(List list) {
        list.add(Progression.translate(getProvider().getUnlocalisedName() + ".execute"));
        list.add(TextFormatting.GRAY + command);
    }

    @Override
    public void reward(EntityPlayerMP player) {
        String newCommand = command.replace("@u", player.getDisplayNameString());
        FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(FakeOp.getInstance(), newCommand);
    }
}
