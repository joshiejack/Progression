package joshie.progression.network;

import io.netty.buffer.ByteBuf;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.handlers.APICache;
import joshie.progression.network.core.PenguinPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.achievement.GuiAchievement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.Achievement;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

@Packet(isSided = true, side = Side.CLIENT)
public class PacketCompleted extends PenguinPacket {
    private ICriteria criteria;

    public PacketCompleted() {}

    public PacketCompleted(ICriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, criteria.getUniqueID().toString());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        criteria = APICache.getClientCache().getCriteria(UUID.fromString(ByteBufUtils.readUTF8String(buf)));
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        if (criteria != null) {
            GuiAchievement gui = Minecraft.getMinecraft().guiAchievement;
            gui.displayUnformattedAchievement(new DummyAchievement(criteria));
            ReflectionHelper.setPrivateValue(GuiAchievement.class, gui, Minecraft.getSystemTime(), "notificationTime", "field_146263_l", "l");
            ReflectionHelper.setPrivateValue(GuiAchievement.class, gui, false, "permanentNotification", "field_146262_n", "n");
        }
    }

    public static class DummyAchievement extends Achievement {
        private final ICriteria criteria;

        public DummyAchievement(ICriteria criteria) {
            super("criteria", "criteria", 0, 0, criteria.getIcon(), null);
            this.criteria = criteria;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public String getDescription() {
            return criteria.getLocalisedName();
        }

        @Override
        public boolean getSpecial() {
            return true;
        }
    }
}
