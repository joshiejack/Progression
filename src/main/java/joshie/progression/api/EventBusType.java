package joshie.progression.api;

/** When registering rewards and triggers, their classes
 *  will automatically get added to the event buses, when they
 *  are created, this enum determines which event bus they should
 *  get registered to. */
public enum EventBusType {
    FML, FORGE, TERRAIN, ORE, NONE;
}

