package joshie.progression.json;

import java.util.List;

public class DataCriteria {
    long timestamp;
    String uniqueName;
    String displayName;
    String displayStack;
    List<DataTrigger> triggers;
    List<DataGeneric> rewards;
    String[] prereqs;
    String[] conflicts;
    int repeatable;
    boolean infinite;
    int tasksRequired;
    boolean allTasks;
    int x;
    int y;
    boolean isVisible;
    boolean mustClaim;
    boolean displayAchievement;
    public int rewardsGiven;
    public boolean allRewards;
}
