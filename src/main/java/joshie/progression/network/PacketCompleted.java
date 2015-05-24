package joshie.progression.network;

import io.netty.buffer.ByteBuf;
import joshie.progression.criteria.Criteria;
import joshie.progression.handlers.APIHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.achievement.GuiAchievement;
import net.minecraft.stats.Achievement;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PacketCompleted implements IMessage, IMessageHandler<PacketCompleted, IMessage> {
    private Criteria criteria;

    public PacketCompleted() {}

    public PacketCompleted(Criteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, criteria.uniqueName);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        criteria = APIHandler.getCriteriaFromName(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public IMessage onMessage(PacketCompleted message, MessageContext ctx) {
        GuiAchievement gui = Minecraft.getMinecraft().guiAchievement;
        gui.func_146255_b(new DummyAchievement(message.criteria));
        ReflectionHelper.setPrivateValue(GuiAchievement.class, gui, Minecraft.getSystemTime(), "field_146263_l");
        ReflectionHelper.setPrivateValue(GuiAchievement.class, gui, false, "field_146262_n");
        return null;
    }
    
    public static class DummyAchievement extends Achievement {
        private final Criteria criteria;
        
        public DummyAchievement(Criteria criteria) {
            super("criteria", "criteria", 0, 0, criteria.stack, null);
            this.criteria = criteria;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public String getDescription() {
            return criteria.displayName;
        }
    }
}
