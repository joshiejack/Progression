package joshie.crafting.events;

import joshie.crafting.helpers.DataHelper;
import joshie.crafting.helpers.PlayerHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EntityEvents {
	@SubscribeEvent
	public void onDeath(LivingDeathEvent event) {
		Entity source = event.source.getSourceOfDamage();
		if (source != null && source instanceof EntityPlayer) {
			DataHelper.getData().setKilled(PlayerHelper.getUUIDForPlayer((EntityPlayer)source), EntityList.getEntityString(event.entity));
		}
	}
}
