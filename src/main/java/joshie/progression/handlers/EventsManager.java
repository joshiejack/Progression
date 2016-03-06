package joshie.progression.handlers;

import java.util.HashSet;

import joshie.progression.api.EventBusType;
import joshie.progression.api.IRewardType;
import joshie.progression.api.ITriggerType;
import joshie.progression.criteria.Reward;
import joshie.progression.criteria.Trigger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class EventsManager {
    public static HashSet<Trigger> activeTriggers;
    public static HashSet<Reward> activeRewards;

    public static EventBus getBus(EventBusType bus) {
        if (bus == EventBusType.FML) {
            return FMLCommonHandler.instance().bus();
        } else if (bus == EventBusType.FORGE) {
            return MinecraftForge.EVENT_BUS;
        } else if (bus == EventBusType.ORE) {
            return MinecraftForge.ORE_GEN_BUS;
        } else if (bus == EventBusType.TERRAIN) {
            return MinecraftForge.TERRAIN_GEN_BUS;
        } else return null;
    }

    public static void onTriggerAdded(Trigger trigger) {
        activeTriggers.add(trigger); //Add the new trigger
        HashSet activeTriggerTypes = new HashSet();
        for (Trigger existing : activeTriggers) { //Grab a list of all the triggers that should be registered
            activeTriggerTypes.add(existing.getType().getUnlocalisedName());
        }

        for (ITriggerType type : APIHandler.triggerTypes.values()) { //Loop through all trigger types
            if (activeTriggerTypes.contains(type.getUnlocalisedName())) { //If we haven't added this type to active triggers yet add it
                try {
                    EventBusType[] buses = type.getEventBusTypes();
                    for (EventBusType bus : buses) {
                        EventBus theBus = getBus(bus);
                        if (theBus != null) theBus.register(type);
                    }
                } catch (Exception e) {}
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
                        EventBus theBus = getBus(bus);
                        if (theBus != null) theBus.unregister(type);
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
                try {
                    EventBusType[] buses = type.getEventBusTypes();
                    for (EventBusType bus : buses) {
                        EventBus theBus = getBus(bus);
                        if (theBus != null) theBus.register(type);
                    }
                } catch (Exception e) {}
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
                        EventBus theBus = getBus(bus);
                        if (theBus != null) theBus.unregister(type);
                    }
                } catch (Exception e) {}
            }
        }
    }
}
