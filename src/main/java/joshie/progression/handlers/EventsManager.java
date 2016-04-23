package joshie.progression.handlers;

import joshie.progression.Progression;
import joshie.progression.api.criteria.*;
import joshie.progression.api.special.IHasEventBus;
import joshie.progression.json.Options;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class EventsManager {
    private static HashSet<IRule> active;

    public static void create() {
        if (active != null) {
            for (IRule type: active) {
                try {
                    if (type instanceof IHasEventBus) {
                        EventBus bus = ((IHasEventBus) type).getEventBus();
                        if (bus != null) {
                            if (Options.debugMode) Progression.logger.log(Level.INFO, "Unregistered the object " + type + " from the event bus");
                            bus.unregister(type);
                        }
                    }
                } catch (Exception e) {}
            }
        }

        active = new HashSet();
    }


    public static void onAdded(IRule eventType) {
        active.add(getGenericFromType(eventType));
        HashSet activeTypes = new HashSet();
        for (IRule existing: active) {
            activeTypes.add(existing.getProvider().getUnlocalisedName());
        }

        for (IRuleProvider provider: getCollectionFromType(eventType)) {
            if (activeTypes.contains(provider.getUnlocalisedName())) {
                try {
                    IRule type = provider.getProvided();
                    if (type instanceof IHasEventBus) {
                        EventBus bus = ((IHasEventBus) type).getEventBus();
                        if (bus != null) {
                            bus.register(type);
                        }
                    }
                } catch (Exception e) {}
            }
        }

        if (eventType instanceof IReward) {
            ((IReward)eventType).onAdded();
        }
    }

    public static void onRemoved(IRule eventType) {
        active.remove(getGenericFromType(eventType));
        HashSet activeTypes = new HashSet();
        for (IRule existing: active) {
            activeTypes.add(existing.getProvider().getUnlocalisedName());
        }

        for (IRuleProvider provider: getCollectionFromType(eventType)) {
            if (!activeTypes.contains(provider.getUnlocalisedName())) {
                try {
                    IRule type = provider.getProvided();
                    if (type instanceof IHasEventBus) {
                        EventBus bus = ((IHasEventBus) type).getEventBus();
                        if (bus != null) {
                            bus.unregister(type);
                        }
                    }
                } catch (Exception e) {}
            }
        }

        if (eventType instanceof IReward) {
            ((IReward)eventType).onRemoved();
        }
    }

    private static IRule getGenericFromType(IRule type) {
        if (type instanceof ITrigger) return APIHandler.triggerTypes.get(type.getProvider().getUnlocalisedName()).getProvided();
        else if (type instanceof IReward) return APIHandler.rewardTypes.get(type.getProvider().getUnlocalisedName()).getProvided();
        else if (type instanceof IFilter) return APIHandler.filterTypes.get(type.getProvider().getUnlocalisedName()).getProvided();
        else if (type instanceof ICondition) return APIHandler.conditionTypes.get(type.getProvider().getUnlocalisedName()).getProvided();
        else return null; //Will never return null;
    }

    private static Collection<IRuleProvider> getCollectionFromType(IRule type) {
        if (type instanceof ITrigger) return new ArrayList(APIHandler.triggerTypes.values());
        else if (type instanceof IReward) return new ArrayList(APIHandler.rewardTypes.values());
        else if (type instanceof ICondition) return new ArrayList(APIHandler.conditionTypes.values());
        else if (type instanceof IFilter) return new ArrayList(APIHandler.filterTypes.values());
        else return null; //Will never return null;
    }
}
