package joshie.progression.criteria.filters.entity;

import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.helpers.ListHelper;
import joshie.progression.helpers.PlayerHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

@ProgressionRule(name="online", color=0xFFB25900)
public class FilterOnlinePlayers extends FilterBaseEntity {
    @Override
    public List<EntityLivingBase> getRandom(EntityPlayer player) {
        return ListHelper.newArrayList(PlayerHelper.getAllPlayers());
    }

    @Override
    protected boolean matches(EntityLivingBase entity) {
        return entity instanceof EntityPlayer;
    }
}