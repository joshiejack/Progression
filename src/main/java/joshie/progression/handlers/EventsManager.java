package joshie.progression.handlers;

import java.util.HashSet;

import joshie.progression.api.IHasEventBus;
import joshie.progression.api.IRewardType;
import joshie.progression.api.ITriggerType;
import net.minecraftforge.fml.common.eventhandler.EventBus;

public class EventsManager {
    private static HashSet<ITriggerType> activeTriggers;
    private static HashSet<IRewardType> activeRewards;

    public static void create() {
        activeRewards = new HashSet(); //Reset active rewards
        activeTriggers = new HashSet(); //Reset active triggers
    }

    public static void onTriggerAdded(ITriggerType trigger) {
        activeTriggers.add(trigger); //Add the new trigger
        HashSet activeTriggerTypes = new HashSet();
        for (ITriggerType existing : activeTriggers) { //Grab a list of all the triggers that should be registered
            activeTriggerTypes.add(existing.getUnlocalisedName());
        }

        for (ITriggerType type : APIHandler.triggerTypes.values()) { //Loop through all trigger types
            if (activeTriggerTypes.contains(type.getUnlocalisedName())) { //If we haven't added this type to active triggers yet add it
                try {
                    if (type instanceof IHasEventBus) {
                        EventBus bus = ((IHasEventBus) type).getEventBus();
                        if (bus != null) bus.register(type);
                    }
                } catch (Exception e) {}
            }
        }
    }

    public static void onTriggerRemoved(ITriggerType trigger) {
        activeTriggers.remove(trigger); //Add the new trigger
        HashSet activeTriggerTypes = new HashSet();
        for (ITriggerType existing : activeTriggers) { //Grab a list of all the triggers that should be registered
            activeTriggerTypes.add(existing.getUnlocalisedName());
        }

        for (ITriggerType type : APIHandler.triggerTypes.values()) { //Loop through all trigger types
            if (!activeTriggerTypes.contains(type.getUnlocalisedName())) { //If this trigger type is no longer in the active ones, unregister it
                try {
                    if (type instanceof IHasEventBus) {
                        EventBus bus = ((IHasEventBus) type).getEventBus();
                        if (bus != null) bus.unregister(type);
                    }
                } catch (Exception e) {}
            }
        }
    }

    public static void onRewardAdded(IRewardType reward) {
        activeRewards.add(reward); //Add the new reward
        HashSet activeRewardTypes = new HashSet();
        for (IRewardType existing : activeRewards) { //Grab a list of all the rewards that should be registered
            activeRewardTypes.add(existing.getUnlocalisedName());
        }

        for (IRewardType type : APIHandler.rewardTypes.values()) { //Loop through all reward types
            if (activeRewardTypes.contains(type.getUnlocalisedName())) { //If we haven't added this type to active rewards yet add it
                try {
                    if (type instanceof IHasEventBus) {
                        EventBus bus = ((IHasEventBus) type).getEventBus();
                        if (bus != null) bus.register(type);
                    }
                } catch (Exception e) {}
            }
        }
        
        reward.onAdded();
    }

    public static void onRewardRemoved(IRewardType reward) {
        activeRewards.remove(reward); //Add the new reward
        HashSet activeRewardTypes = new HashSet();
        for (IRewardType existing : activeRewards) { //Grab a list of all the rewards that should be registered
            activeRewardTypes.add(existing.getUnlocalisedName());
        }

        for (IRewardType type : APIHandler.rewardTypes.values()) { //Loop through all reward types
            if (!activeRewardTypes.contains(type.getUnlocalisedName())) { //If this reward type is no longer in the active ones, unregister it
                try {
                    if (type instanceof IHasEventBus) {
                        EventBus bus = ((IHasEventBus) type).getEventBus();
                        if (bus != null) bus.unregister(type);
                    }
                } catch (Exception e) {}
            }
        }
        
        reward.onRemoved();
    }
}
