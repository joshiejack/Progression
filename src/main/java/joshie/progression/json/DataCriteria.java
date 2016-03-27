package joshie.progression.json;

import java.util.List;
import java.util.UUID;

public class DataCriteria {
    long timestamp;
    UUID uuid;
    String displayName;
    String displayStack;
    List<DataTrigger> triggers;
    List<DataGeneric> rewards;
    UUID[] prereqs;
    UUID[] conflicts;
    int repeatable;
    boolean infinite;
    int tasksRequired;
    boolean allTasks;
    int x;
    int y;
    boolean isVisible;
    boolean displayAchievement;
    public int rewardsGiven;
    public boolean allRewards;
}
