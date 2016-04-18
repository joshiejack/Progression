package joshie.progression.helpers;

import com.google.common.collect.Multimap;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.Collection;
import java.util.Map;

public class NBTHelper {
    public static void readTagCollection(NBTTagCompound nbt, String string, ICollectionHelper something) {
        readCollection(nbt, string, something, 10);
    }

    public static void readStringCollection(NBTTagCompound nbt, String string, ICollectionHelper something) {
        readCollection(nbt, string, something, 8);
    }

    private static void readCollection(NBTTagCompound nbt, String string, ICollectionHelper something, int num) {
        NBTTagList list = nbt.getTagList(string, num);
        for (int i = 0; i < list.tagCount(); i++) {
            Object o = something.read(list, i);
            if (o != null) {
                something.getSet().add(o);
            }
        }
    }

    public static NBTTagCompound writeCollection(NBTTagCompound nbt, String name, ICollectionHelper something) {
        NBTTagList list = new NBTTagList();
        for (Object s : something.getSet()) {
            if (s == null) continue;
            list.appendTag(something.write(s));
        }

        nbt.setTag(name, list);
        return nbt;
    }

    public static void readMap(NBTTagCompound nbt, String string, IMapHelper something) {
        NBTTagList list = nbt.getTagList(string, 10);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            Object key = something.readKey(tag);
            Object data = something.readValue(tag);
            if (key == null || data == null) continue;
            something.getMap().put(key, data);
        }
    }

    public static NBTTagCompound writeMap(NBTTagCompound nbt, String name, IMapHelper something) {
        NBTTagList list = new NBTTagList();
        for (Object o: something.getMap().keySet()) {
            Object key = o;
            Object value = something.getMap().get(key);
            if (key == null || value == null) continue;
            NBTTagCompound tag = new NBTTagCompound();
            something.writeKey(tag, key);
            something.writeValue(tag, value);
            list.appendTag(tag);
        }

        nbt.setTag(name, list);
        return nbt;
    }


    public static void readMultimap(NBTTagCompound nbt, String string, IMultimapHelper something) {
        NBTTagList list = nbt.getTagList(string, 10);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            Object key = something.readKey(tag);
            if (key == null) continue;
            NBTTagList set = tag.getTagList("Set", 10);
            for (int j = 0; j < set.tagCount(); j++) {
                NBTTagCompound tagCompound = set.getCompoundTagAt(j);
                Object data = something.readValue(tagCompound);
                if (data == null) continue;
                something.getMap().get(key).add(data);
            }
        }
    }

    public static NBTTagCompound writeMultimap(NBTTagCompound nbt, String name, IMultimapHelper something) {
        NBTTagList list = new NBTTagList();
        for (Object o: something.getMap().keySet()) {
            Object key = o;
            Object value = something.getMap().get(key);
            if (key == null || value == null) continue;
            NBTTagCompound tag = new NBTTagCompound();
            something.writeKey(tag, key);
            NBTTagList tagList = new NBTTagList();
            for (Object object: something.getMap().get(key)) {
                NBTTagCompound tagCompound = new NBTTagCompound();
                something.writeValue(tagCompound, object);
                tagList.appendTag(tagCompound);
            }

            tag.setTag("Set", tagList);
            list.appendTag(tag);
        }

        nbt.setTag(name, list);
        return nbt;
    }

    public static interface ICollectionHelper<T> {
        Collection getSet();
        T read(NBTTagList list, int i);
        NBTBase write(T s);
    }

    public static interface IMapHelper<K, V> {
        Map<K, V> getMap();
        void writeKey(NBTTagCompound tag, K o);
        K readKey(NBTTagCompound tag);
        V readValue(NBTTagCompound tag);
        void writeValue(NBTTagCompound tag, V o);
    }

    public static interface IMultimapHelper<K, V> {
        Multimap<K, V> getMap();
        void writeKey(NBTTagCompound tag, K o);
        K readKey(NBTTagCompound tag);
        V readValue(NBTTagCompound tag);
        void writeValue(NBTTagCompound tag, V o);
    }
}
