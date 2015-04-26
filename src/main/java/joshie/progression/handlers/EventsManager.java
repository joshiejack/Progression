package joshie.progression.handlers;

import java.util.HashSet;

import joshie.progression.api.EventBusType;
import joshie.progression.api.IRewardType;
import joshie.progression.api.ITriggerType;
import joshie.progression.criteria.Reward;
import joshie.progression.criteria.Trigger;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;

public class EventsManager {
    public static HashSet<Trigger> activeTriggers;
    public static HashSet<Reward> activeRewards;

    public static void onTriggerAdded(Trigger trigger) {
        activeTriggers.add(trigger); //Add the new trigger
        HashSet activeTriggerTypes = new HashSet();
        for (Trigger existing : activeTriggers) { //Grab a list of all the triggers that should be registered
            activeTriggerTypes.add(existing.getType().getUnlocalisedName());
        }

        for (ITriggerType type : APIHandler.triggerTypes.values()) { //Loop through all trigger types
            if (activeTriggerTypes.contains(type.getUnlocalisedName())) { //If we haven't added this type to active triggers yet add it
                EventBusType[] buses = type.getEventBusTypes();
                for (EventBusType bus : buses) {
                    if (bus == EventBusType.FML) {
                        FMLCommonHandler.instance().bus().register(type);
                    } else if (bus == EventBusType.FORGE) {
                        MinecraftForge.EVENT_BUS.register(type);
                    } else if (bus == EventBusType.ORE) {
                        MinecraftForge.ORE_GEN_BUS.register(type);
                    } else if (bus == EventBusType.TERRAIN) {
                        MinecraftForge.TERRAIN_GEN_BUS.register(type);
                    }
                }
            }
        }
    }

    public static void onTriggerRemoved(Trigger trigger) {
        activeTriggers.remove(trigger); //Add the new trigger
        HashSet activeTriggerTypes = new HashSet();
        for (Trigger existing : activeTriggers) { //Grab a list of all the triggers that should be registered
            activeTriggerTypes.add(existing.getType().getUnlocalisedName());
        }

        for (ITriggerType type : APIHandler.triggerTypes.values()) { //Loop through all trigger types
            if (!activeTriggerTypes.contains(type.getUnlocalisedName())) { //If this trigger type is no longer in the active ones, unregister it
                try {
                    EventBusType[] buses = type.getEventBusTypes();
                    for (EventBusType bus : buses) {
                        if (bus == EventBusType.FML) {
                            FMLCommonHandler.instance().bus().unregister(type);
                        } else if (bus == EventBusType.FORGE) {
                            MinecraftForge.EVENT_BUS.unregister(type);
                        } else if (bus == EventBusType.ORE) {
                            MinecraftForge.ORE_GEN_BUS.unregister(type);
                        } else if (bus == EventBusType.TERRAIN) {
                            MinecraftForge.TERRAIN_GEN_BUS.unregister(type);
                        }
                    }
                } catch (Exception e) {}
            }
        }
    }

    public static void onRewardAdded(Reward reward) {
        activeRewards.add(reward); //Add the new reward
        HashSet activeRewardTypes = new HashSet();
        for (Reward existing : activeRewards) { //Grab a list of all the rewards that should be registered
            activeRewardTypes.add(existing.getType().getUnlocalisedName());
        }

        for (IRewardType type : APIHandler.rewardTypes.values()) { //Loop through all reward types
            if (activeRewardTypes.contains(type.getUnlocalisedName())) { //If we haven't added this type to active rewards yet add it
                EventBusType[] buses = type.getEventBusTypes();
                for (EventBusType bus : buses) {
                    if (bus == EventBusType.FML) {
                        FMLCommonHandler.instance().bus().register(type);
                    } else if (bus == EventBusType.FORGE) {
                        MinecraftForge.EVENT_BUS.register(type);
                    } else if (bus == EventBusType.ORE) {
                        MinecraftForge.ORE_GEN_BUS.register(type);
                    } else if (bus == EventBusType.TERRAIN) {
                        MinecraftForge.TERRAIN_GEN_BUS.register(type);
                    }
                }
            }
        }
    }

    public static void onRewardRemoved(Reward reward) {
        activeRewards.remove(reward); //Add the new reward
        HashSet activeRewardTypes = new HashSet();
        for (Reward existing : activeRewards) { //Grab a list of all the rewards that should be registered
            activeRewardTypes.add(existing.getType().getUnlocalisedName());
        }

        for (IRewardType type : APIHandler.rewardTypes.values()) { //Loop through all reward types
            if (!activeRewardTypes.contains(type.getUnlocalisedName())) { //If this reward type is no longer in the active ones, unregister it
                try {
                    EventBusType[] buses = type.getEventBusTypes();
                    for (EventBusType bus : buses) {
                        if (bus == EventBusType.FML) {
                            FMLCommonHandler.instance().bus().unregister(type);
                        } else if (bus == EventBusType.FORGE) {
                            MinecraftForge.EVENT_BUS.unregister(type);
                        } else if (bus == EventBusType.ORE) {
                            MinecraftForge.ORE_GEN_BUS.unregister(type);
                        } else if (bus == EventBusType.TERRAIN) {
                            MinecraftForge.TERRAIN_GEN_BUS.unregister(type);
                        }
                    }
                } catch (Exception e) {}
            }
        }
    }
}
