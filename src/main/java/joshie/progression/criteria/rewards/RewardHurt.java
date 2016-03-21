package joshie.progression.criteria.rewards;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import joshie.progression.api.IGetterCallback;
import joshie.progression.api.fields.IInit;
import joshie.progression.helpers.PlayerHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;

public class RewardHurt extends RewardBase implements IInit, IGetterCallback {
    public static final HashMap<String, DamageSource> sources = new HashMap();
    private DamageSource source;
    public String damagesource = "magic";
    public float damage = 1F;

    public RewardHurt() {
        super("hurt", 0xFFE599FF);
    }
    
    @Override
    public void init() {
        if (damagesource.equals("magic")) source = DamageSource.magic;
        else {
            source = sources.get(damagesource);
        }
    }
    
    @Override
    public String getField(String fieldName) {
        return source != null ? EnumChatFormatting.GREEN + damagesource : EnumChatFormatting.RED + damagesource;
    }
        
    @Override
    public void reward(UUID uuid) {
        if (source == null) return;
        List<EntityPlayerMP> players = PlayerHelper.getPlayersFromUUID(uuid);
        for (EntityPlayerMP player : players) {
            if (player != null) {
                player.attackEntityFrom(source, damage);
            }
        }
    }

    @Override
    public void addTooltip(List list) {
        // list.add(EnumChatFormatting.WHITE + Progression.translate("item.free"));
        // list.add(getIcon().getDisplayName() + " x" + spawnNumber);
    }
}
