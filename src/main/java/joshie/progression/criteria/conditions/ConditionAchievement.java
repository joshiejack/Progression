package joshie.progression.criteria.conditions;

import joshie.progression.api.IPlayerTeam;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.IField;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.*;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import org.apache.commons.lang3.text.WordUtils;

import java.util.List;

@ProgressionRule(name="achievement", color=0xFFFFFF00, meta="ifHasAchievement")
public class ConditionAchievement extends ConditionBase implements IInit, ICustomWidth, ISpecialFieldProvider, IItemGetterCallback, IAdditionalTooltip {
    public String id = "mineWood";
    private transient Achievement achievement;

    @Override
    public void init() {
        for (Achievement a: AchievementList.achievementList) {
            if (a.statId.equals("achievement." + id)) {
                achievement = a;
                break;
            }
        }
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.EDIT ? 100 : 92;
    }

    @Override
    public void addSpecialFields(List<IField> fields, DisplayMode mode) {
        if (mode == DisplayMode.DISPLAY) fields.add(ProgressionAPI.fields.getItem(this, "id", 20, 42, 2F));
    }

    @Override
    public ItemStack getItem(String fieldName) {
        return achievement != null ? achievement.theItemStack : new ItemStack(Items.golden_hoe);
    }

    @Override
    public void addHoverTooltip(List<String> tooltip) {
        tooltip.clear();
        if (achievement != null) {
            tooltip.add(EnumChatFormatting.DARK_AQUA + StatCollector.translateToLocal(achievement.statId));
            String[] split = WordUtils.wrap(achievement.getDescription() + ".", 27).split("\n");
            for (String s: split) {
                tooltip.add(s.trim());
            }
        }
    }
    
    @Override
    public boolean isSatisfied(IPlayerTeam team) {
        if (achievement != null) {
            for (EntityPlayer player: team.getTeamEntities()) { //If any team member has the achievement
                if (player.worldObj.isRemote && ((EntityPlayerSP)player).getStatFileWriter().hasAchievementUnlocked(achievement)) return true;
                else if (!player.worldObj.isRemote && ((EntityPlayerMP)player).getStatFile().hasAchievementUnlocked(achievement)) return true;
            }
        }

        return false;
    }
}
