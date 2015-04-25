package joshie.crafting;

import java.util.HashSet;

import joshie.crafting.api.Bus;
import joshie.crafting.api.IRewardType;
import joshie.crafting.api.ITriggerType;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;

public class CraftingEventsManager {
    public static HashSet<Trigger> activeTriggers;
    public static HashSet<Reward> activeRewards;

    public static void onTriggerAdded(Trigger trigger) {
        activeTriggers.add(trigger); //Add the new trigger
        HashSet activeTriggerTypes = new HashSet();
        for (Trigger existing : activeTriggers) { //Grab a list of all the triggers that should be registered
            activeTriggerTypes.add(existing.getType().getUnlocalisedName());
        }

        for (ITriggerType type : CraftAPIRegistry.triggerTypes.values()) { //Loop through all trigger types
            if (activeTriggerTypes.contains(type.getUnlocalisedName())) { //If we haven't added this type to active triggers yet add it
                Bus[] buses = type.getEventBusTypes();
                for (Bus bus : buses) {
                    if (bus == Bus.FML) {
                        FMLCommonHandler.instance().bus().register(type);
                    } else if (bus == Bus.FORGE) {
                        MinecraftForge.EVENT_BUS.register(type);
                    } else if (bus == Bus.ORE) {
                        MinecraftForge.ORE_GEN_BUS.register(type);
                    } else if (bus == Bus.TERRAIN) {
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

        for (ITriggerType type : CraftAPIRegistry.triggerTypes.values()) { //Loop through all trigger types
            if (!activeTriggerTypes.contains(type.getUnlocalisedName())) { //If this trigger type is no longer in the active ones, unregister it
                try {
                    Bus[] buses = type.getEventBusTypes();
                    for (Bus bus : buses) {
                        if (bus == Bus.FML) {
                            FMLCommonHandler.instance().bus().unregister(type);
                        } else if (bus == Bus.FORGE) {
                            MinecraftForge.EVENT_BUS.unregister(type);
                        } else if (bus == Bus.ORE) {
                            MinecraftForge.ORE_GEN_BUS.unregister(type);
                        } else if (bus == Bus.TERRAIN) {
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

        for (IRewardType type : CraftAPIRegistry.rewardTypes.values()) { //Loop through all reward types
            if (activeRewardTypes.contains(type.getUnlocalisedName())) { //If we haven't added this type to active rewards yet add it
                Bus[] buses = type.getEventBusTypes();
                for (Bus bus : buses) {
                    if (bus == Bus.FML) {
                        FMLCommonHandler.instance().bus().register(type);
                    } else if (bus == Bus.FORGE) {
                        MinecraftForge.EVENT_BUS.register(type);
                    } else if (bus == Bus.ORE) {
                        MinecraftForge.ORE_GEN_BUS.register(type);
                    } else if (bus == Bus.TERRAIN) {
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

        for (IRewardType type : CraftAPIRegistry.rewardTypes.values()) { //Loop through all reward types
            if (!activeRewardTypes.contains(type.getUnlocalisedName())) { //If this reward type is no longer in the active ones, unregister it
                try {
                    Bus[] buses = type.getEventBusTypes();
                    for (Bus bus : buses) {
                        if (bus == Bus.FML) {
                            FMLCommonHandler.instance().bus().unregister(type);
                        } else if (bus == Bus.FORGE) {
                            MinecraftForge.EVENT_BUS.unregister(type);
                        } else if (bus == Bus.ORE) {
                            MinecraftForge.ORE_GEN_BUS.unregister(type);
                        } else if (bus == Bus.TERRAIN) {
                            MinecraftForge.TERRAIN_GEN_BUS.unregister(type);
                        }
                    }
                } catch (Exception e) {}
            }
        }
    }
}
