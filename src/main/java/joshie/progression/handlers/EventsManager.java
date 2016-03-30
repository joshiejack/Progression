package joshie.progression.handlers;

import joshie.progression.Progression;
import joshie.progression.api.criteria.ICanHaveEvents;
import joshie.progression.api.criteria.IProgressionReward;
import joshie.progression.api.special.IHasEventBus;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.apache.logging.log4j.Level;

import java.util.HashSet;

public class EventsManager {
    private static HashSet<ICanHaveEvents> active;

    public static void create() {
        if (active != null) {
            for (ICanHaveEvents type: active) {
                try {
                    if (type instanceof IHasEventBus) {
                        EventBus bus = ((IHasEventBus) type).getEventBus();
                        if (bus != null) {
                            Progression.logger.log(Level.INFO, "Unregistered the object " + type + " from the event bus");
                            bus.unregister(type);
                        }
                    }
                } catch (Exception e) {}
            }
        }

        active = new HashSet();
    }


    public static void onAdded(ICanHaveEvents eventType) {
        active.add(APIHandler.getGenericFromType(eventType));
        HashSet activeTypes = new HashSet();
        for (ICanHaveEvents existing: active) {
            activeTypes.add(existing.getUnlocalisedName());
        }

        for (ICanHaveEvents type: APIHandler.getCollectionFromType(eventType)) {
            if (activeTypes.contains(type.getUnlocalisedName())) {
                try {
                    if (type instanceof IHasEventBus) {
                        EventBus bus = ((IHasEventBus) type).getEventBus();
                        if (bus != null) {
                            bus.register(type);
                        }
                    }
                } catch (Exception e) {}
            }
        }

        if (eventType instanceof IProgressionReward) {
            ((IProgressionReward)eventType).onAdded();
        }
    }

    public static void onRemoved(ICanHaveEvents eventType) {
        active.remove(APIHandler.getGenericFromType(eventType));
        HashSet activeTypes = new HashSet();
        for (ICanHaveEvents existing: active) {
            activeTypes.add(existing.getUnlocalisedName());
        }

        for (ICanHaveEvents type: APIHandler.getCollectionFromType(eventType)) {
            if (!activeTypes.contains(type.getUnlocalisedName())) {
                try {
                    if (type instanceof IHasEventBus) {
                        EventBus bus = ((IHasEventBus) type).getEventBus();
                        if (bus != null) {
                            bus.unregister(type);
                        }
                    }
                } catch (Exception e) {}
            }
        }

        if (eventType instanceof IProgressionReward) {
            ((IProgressionReward)eventType).onRemoved();
        }
    }
}
