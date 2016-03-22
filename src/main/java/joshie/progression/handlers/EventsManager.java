package joshie.progression.handlers;

import java.util.HashSet;

import joshie.progression.api.criteria.IProgressionReward;
import joshie.progression.api.criteria.IProgressionTrigger;
import joshie.progression.api.special.IHasEventBus;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventBus;

public class EventsManager {
    private static HashSet<IProgressionTrigger> activeTriggers;
    private static HashSet<IProgressionReward> activeRewards;

    public static void create() {
        //Before we reset everything we should unregister them all
        if (activeRewards != null) {
            for (IProgressionReward type : activeRewards) {
                try {
                    if (type instanceof IHasEventBus) {
                        EventBus bus = ((IHasEventBus) type).getEventBus();
                        if (bus != null) bus.unregister(type);
                    }
                } catch (Exception e) {}
            }
        }

        if (activeTriggers != null) {
            for (IProgressionTrigger type : activeTriggers) {
                try {
                    if (type instanceof IHasEventBus) {
                        EventBus bus = ((IHasEventBus) type).getEventBus();
                        if (bus != null) bus.unregister(type);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } 

        activeRewards = new HashSet(); //Reset active rewards
        activeTriggers = new HashSet(); //Reset active triggers
    }

    public static void onTriggerAdded(IProgressionTrigger trigger) {
        activeTriggers.add(APIHandler.triggerTypes.get(trigger.getUnlocalisedName())); //Add the new trigger
        HashSet activeTriggerTypes = new HashSet();
        for (IProgressionTrigger existing : activeTriggers) { //Grab a list of all the triggers that should be registered
            activeTriggerTypes.add(existing.getUnlocalisedName());
        }

        for (IProgressionTrigger type : APIHandler.triggerTypes.values()) { //Loop through all trigger types
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

    public static void onTriggerRemoved(IProgressionTrigger trigger) {
        activeTriggers.remove(APIHandler.triggerTypes.get(trigger.getUnlocalisedName())); //Add the new trigger
        HashSet activeTriggerTypes = new HashSet();
        for (IProgressionTrigger existing : activeTriggers) { //Grab a list of all the triggers that should be registered
            activeTriggerTypes.add(existing.getUnlocalisedName());
        }

        for (IProgressionTrigger type : APIHandler.triggerTypes.values()) { //Loop through all trigger types
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

    public static void onRewardAdded(IProgressionReward reward) {
        activeRewards.add(APIHandler.rewardTypes.get(reward.getUnlocalisedName())); //Add the new reward
        HashSet activeRewardTypes = new HashSet();
        for (IProgressionReward existing : activeRewards) { //Grab a list of all the rewards that should be registered
            activeRewardTypes.add(existing.getUnlocalisedName());
        }

        for (IProgressionReward type : APIHandler.rewardTypes.values()) { //Loop through all reward types
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

    public static void onRewardRemoved(IProgressionReward reward) {
        activeRewards.remove(APIHandler.rewardTypes.get(reward.getUnlocalisedName())); //Add the new reward
        HashSet activeRewardTypes = new HashSet();
        for (IProgressionReward existing : activeRewards) { //Grab a list of all the rewards that should be registered
            activeRewardTypes.add(existing.getUnlocalisedName());
        }

        for (IProgressionReward type : APIHandler.rewardTypes.values()) { //Loop through all reward types
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
