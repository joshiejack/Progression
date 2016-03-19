package joshie.progression.criteria.conditions;

import java.util.UUID;

import joshie.progression.api.IInitAfterRead;
import joshie.progression.api.ISetterCallback;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.world.World;

public class ConditionAchievement extends ConditionBase implements IInitAfterRead, ISetterCallback {   
    public String name = "mineWood";
    private transient Achievement achievement;
    
    public ConditionAchievement() {
        super("achievement", 0xFFFFFF00);
    }
    
    @Override
    public boolean isSatisfied(World world, EntityPlayer player, UUID uuid) {
        if (achievement != null) {
            return ((EntityPlayerMP)player).getStatFile().hasAchievementUnlocked(achievement);
        } else return false;
    }

    @Override
    public boolean setField(String fieldName, Object object) {
        name = (String) object;
        
        for (Achievement a: AchievementList.achievementList) {
            if (a.statId.equals("achievement." + name)) {
                achievement = a;
                return true;
            }
        }
        
        return false;
    }

    @Override
    public void init() {
        setField("", name);
    }
}
