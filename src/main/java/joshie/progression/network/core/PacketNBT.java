package joshie.progression.network.core;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public abstract class PacketNBT extends PenguinPacket {
    public static interface INBTWritable<T> {
        public T readFromNBT(NBTTagCompound tag);
        public NBTTagCompound writeToNBT(NBTTagCompound tag);
    }
    
    protected NBTTagCompound tag;
    
    public PacketNBT() {}
    public PacketNBT(INBTWritable readable) {
        tag = readable.writeToNBT(new NBTTagCompound());
    }
    
    @Override
    public void toBytes(ByteBuf to) {
        ByteBufUtils.writeTag(to, tag);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        tag = ByteBufUtils.readTag(from);
    }
}
