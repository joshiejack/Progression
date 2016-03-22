package joshie.progression.api.special;

import net.minecraftforge.fml.common.eventhandler.EventBus;

/** Implement this on rewards that require,
 *  being registered to an event bus, return the event bus they need ,
 *  Triggers by default implement this, you can return null if,
 *  you have a trigger that requires no registration */
public interface IHasEventBus {
    /** Return which event bus this should be returned to **/
    public EventBus getEventBus();
}
