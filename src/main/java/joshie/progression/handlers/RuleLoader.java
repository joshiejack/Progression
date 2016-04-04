package joshie.progression.handlers;

import joshie.progression.Progression;
import joshie.progression.api.criteria.*;
import joshie.progression.helpers.StackHelper;
import joshie.progression.ItemProgression.ItemMeta;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;

public class RuleLoader {
    //Borrowed from JEI
    public static void registerRules(@Nonnull ASMDataTable asmDataTable) {
        Class annotationClass = ProgressionRule.class;
        String annotationClassName = annotationClass.getCanonicalName();
        Set<ASMData> asmDatas = asmDataTable.getAll(annotationClassName);
        for (ASMDataTable.ASMData asmData : asmDatas) {
            try {
                Class<?> asmClass = Class.forName(asmData.getClassName());
                Class<? extends IRule> asmInstanceClass = asmClass.asSubclass(IRule.class);
                IRule instance = asmInstanceClass.newInstance();
                Map<String, Object> data = asmData.getAnnotationInfo();
                String mod = (String) data.get("mod");
                if (mod != null && !Loader.isModLoaded(mod)) continue;

                String name = (String) data.get("name");
                int color = (Integer) data.get("color");
                String icon = (String) data.get("icon");
                String meta = (String) data.get("meta");
                boolean isCancelable = (Boolean) data.get("cancelable");

                ItemStack stack = StackHelper.getStackFromString(icon);
                if (stack == null) stack = new ItemStack(Progression.item);
                if (meta != null) {
                    for (ItemMeta item: ItemMeta.values()) {
                        if (name.equalsIgnoreCase(meta)) {
                            stack.setItemDamage(item.ordinal());
                            break;
                        }
                    }
                }

                if (instance instanceof IReward) {
                    APIHandler.registerRewardType(instance, name, color).setIcon(stack);
                } else if (instance instanceof ITrigger) {
                    ITriggerProvider provider = APIHandler.registerTriggerType(instance, name, color).setIcon(stack);
                    if (isCancelable) {
                        provider.setCancelable();
                    }
                } else if (instance instanceof ICondition) {
                    APIHandler.registerConditionType(instance, name, color).setIcon(stack);
                } else if (instance instanceof IFilter) {
                    APIHandler.registerFilterType(instance, name, color);
                }


            } catch (Exception e) {
                Progression.logger.log(Level.ERROR, "Failed to load: {}", asmData.getClassName(), e);
            }
        }
    }
}
