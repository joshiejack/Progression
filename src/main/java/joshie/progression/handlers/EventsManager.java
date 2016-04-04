package joshie.progression.handlers;

import joshie.progression.Progression;
import joshie.progression.api.criteria.IRule;
import joshie.progression.api.criteria.IRuleProvider;
import joshie.progression.api.criteria.IReward;
import joshie.progression.api.special.IHasEventBus;
import joshie.progression.json.Options;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.apache.logging.log4j.Level;

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
        active.add(APIHandler.getGenericFromType(eventType));
        HashSet activeTypes = new HashSet();
        for (IRule existing: active) {
            activeTypes.add(existing.getProvider().getUnlocalisedName());
        }

        for (IRuleProvider provider: APIHandler.getCollectionFromType(eventType)) {
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
        active.remove(APIHandler.getGenericFromType(eventType));
        HashSet activeTypes = new HashSet();
        for (IRule existing: active) {
            activeTypes.add(existing.getProvider().getUnlocalisedName());
        }

        for (IRuleProvider provider: APIHandler.getCollectionFromType(eventType)) {
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
}
