package joshie.progression.helpers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraftforge.common.DimensionManager;

import java.util.concurrent.Callable;

public class DimensionHelper {
    private static final Cache<Integer, String> dimensionNames = CacheBuilder.newBuilder().maximumSize(128).build();

    public static String getDimensionNameFromID(int id) {
        try {
            return dimensionNames.get(id, new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return DimensionManager.getWorld(id).provider.getDimensionName();
                }
            });
        } catch (Exception e) { return "Invalid World"; }
    }
}
