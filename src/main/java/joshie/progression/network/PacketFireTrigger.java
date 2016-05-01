package joshie.progression.network;

import io.netty.buffer.ByteBuf;
import joshie.progression.api.ICustomDataBuilder;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.handlers.APICache;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.network.core.PenguinPacket;
import joshie.progression.player.PlayerTracker;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.HashMap;
import java.util.UUID;

public class PacketFireTrigger extends PenguinPacket {
    private String type;
    private Object[] data;

    public PacketFireTrigger() {}

    public PacketFireTrigger(String type, Object[] data) {
        this.type = type;
        this.data = data;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, type);
        if (data == null || data.length == 0) buf.writeBoolean(false);
        else {
            buf.writeBoolean(true);
            buf.writeInt(data.length);
            for (Object object : data) {
                EnumObjectType type = EnumObjectType.getType(object);
                buf.writeByte(type.ordinal()); //Write the type of object
                switch (type) {
                    case BOOLEAN:
                        buf.writeBoolean((Boolean) object);
                        break;
                    case BYTE:
                        buf.writeByte((Byte) object);
                        break;
                    case SHORT:
                        buf.writeShort((Short)object);
                        break;
                    case INT:
                        buf.writeInt((Integer) object);
                        break;
                    case LONG:
                        buf.writeLong((Long)object);
                        break;
                    case DOUBLE:
                        buf.writeDouble((Double) object);
                        break;
                    case FLOAT:
                        buf.writeFloat((Float) object);
                        break;
                    case ITEMSTACK:
                        ByteBufUtils.writeItemStack(buf, (ItemStack) object);
                        break;
                    case NBT:
                        ByteBufUtils.writeTag(buf, (NBTTagCompound) object);
                        break;
                    case STRING:
                        ByteBufUtils.writeUTF8String(buf, (String) object);
                        break;
                    case CRITERIA:
                        ByteBufUtils.writeUTF8String(buf, ((ICriteria) object).getUniqueID().toString());
                        break;
                    case BLOCK:
                        ResourceLocation blockLocation = Block.blockRegistry.getNameForObject((Block) object);
                        ByteBufUtils.writeUTF8String(buf, blockLocation.getResourceDomain());
                        ByteBufUtils.writeUTF8String(buf, blockLocation.getResourcePath());
                        break;
                    case ITEM:
                        ResourceLocation itemLocation = Item.itemRegistry.getNameForObject((Item) object);
                        ByteBufUtils.writeUTF8String(buf, itemLocation.getResourceDomain());
                        ByteBufUtils.writeUTF8String(buf, itemLocation.getResourcePath());
                        break;
                    case ENTITY:
                        buf.writeInt(((Entity) object).getEntityId());
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        type = ByteBufUtils.readUTF8String(buf);
        if (buf.readBoolean()) {
            int length = buf.readInt();
            data = new Object[length];
            for (int i = 0; i < data.length; i++) {
                EnumObjectType type = EnumObjectType.values()[buf.readByte()]; //Read the type of object
                switch (type) {
                    case BOOLEAN:
                        data[i] = buf.readBoolean();
                        break;
                    case BYTE:
                        data[i] = buf.readByte();
                        break;
                    case SHORT:
                        data[i] = buf.readShort();
                        break;
                    case INT:
                        data[i] = buf.readInt();
                        break;
                    case LONG:
                        data[i] = buf.readLong();
                        break;
                    case DOUBLE:
                        data[i] = buf.readDouble();
                        break;
                    case FLOAT:
                        data[i] = buf.readFloat();
                        break;
                    case STRING:
                        data[i] = ByteBufUtils.readUTF8String(buf);
                        break;
                    case ITEMSTACK:
                        data[i] = ByteBufUtils.readItemStack(buf);
                        break;
                    case NBT:
                        data[i] = ByteBufUtils.readTag(buf);
                        break;
                    case CRITERIA:
                        data[i] = APICache.getServerCache().getCriteria(UUID.fromString(ByteBufUtils.readUTF8String(buf)));
                        break;
                    case BLOCK:
                        data[i] = Block.blockRegistry.getObject(new ResourceLocation(ByteBufUtils.readUTF8String(buf), ByteBufUtils.readUTF8String(buf)));
                        break;
                    case ITEM:
                        data[i] = Item.itemRegistry.getObject(new ResourceLocation(ByteBufUtils.readUTF8String(buf), ByteBufUtils.readUTF8String(buf)));
                        break;
                    case ENTITY:
                        data[i] = MCClientHelper.getMinecraft().theWorld.getEntityByID(buf.readInt());
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private static enum EnumObjectType {
        BOOLEAN, BYTE, SHORT, INT, LONG, DOUBLE, FLOAT, STRING, ITEMSTACK, NBT, CRITERIA, BLOCK, ITEM, ENTITY;

        public static EnumObjectType getType(Object object) {
            if (object.getClass() == boolean.class || object instanceof Boolean) return BOOLEAN;
            if (object.getClass() == byte.class || object instanceof Byte) return BYTE;
            if (object.getClass() == short.class || object instanceof Short) return SHORT;
            if (object.getClass() == int.class || object instanceof Integer) return INT;
            if (object.getClass() == long.class || object instanceof Long) return LONG;
            if (object.getClass() == double.class || object instanceof Double) return DOUBLE;
            if (object.getClass() == float.class || object instanceof Float) return FLOAT;
            if (object instanceof String) return STRING;
            if (object instanceof ItemStack) return ITEMSTACK;
            if (object instanceof NBTTagCompound) return NBT;
            if (object instanceof ICriteria) return CRITERIA;
            if (object instanceof Block) return BLOCK;
            if (object instanceof Item) return ITEM;
            if (object instanceof Entity) return ENTITY;
            return null;
        }
    }
    
    public static HashMap<String, ICustomDataBuilder> handlers = new HashMap();

    @Override
    public void handlePacket(EntityPlayer player) {       
        ICustomDataBuilder builder = handlers.get(type);
        if (builder != null) {
            data = builder.getObjects(player, type, data);
        }

        if (type.equals("complete")) PlayerTracker.getServerPlayer(player).getMappings().forceComplete((ICriteria)data[0]);
        else ProgressionAPI.registry.fireTrigger(player, type, data);
    }
}
