package joshie.progression.criteria.conditions;

import java.util.UUID;

import joshie.progression.api.special.IInit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.world.World;

public class ConditionAchievement extends ConditionBase implements IInit {   
    public String name = "mineWood";
    private transient Achievement achievement;
    
    public ConditionAchievement() {
        super("achievement", 0xFFFFFF00);
    }
    
    @Override
    public void init() {
        for (Achievement a: AchievementList.achievementList) {
            if (a.statId.equals("achievement." + name)) {
                achievement = a;
                break;
            }
        }
    }
    
    @Override
    public boolean isSatisfied(World world, EntityPlayer player, UUID uuid) {
        if (achievement != null) {
            return ((EntityPlayerMP)player).getStatFile().hasAchievementUnlocked(achievement);
        } else return false;
    }
}
