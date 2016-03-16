package joshie.progression.criteria.rewards;

import java.util.List;
import java.util.UUID;

import joshie.progression.Progression;
import joshie.progression.helpers.PlayerHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class RewardTeleport extends RewardBase {
    public int dimension = 0;
    public double x = 0;
    public double y = 64;
    public double z = 0;

    public RewardTeleport() {
        super(new ItemStack(Items.ender_pearl), "teleport", 0xFFDDDDDD);
    }

    @Override
    public void reward(UUID uuid) {
        List<EntityPlayerMP> players = PlayerHelper.getPlayersFromUUID(uuid);
        World world = DimensionManager.getWorld(dimension);
        for (EntityPlayerMP player : players) {
            if (player.worldObj.provider.getDimensionId() != dimension) {
                player.travelToDimension(dimension);
            }

            player.setPosition(x, y, z);
        }
    }

    @Override
    public void addTooltip(List list) {
        list.add(EnumChatFormatting.WHITE + Progression.translate("teleport"));
    }
}
