package joshie.progression.criteria.rewards;

import joshie.progression.api.special.IGetterCallback;
import joshie.progression.api.special.IInit;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;

import java.util.HashMap;
import java.util.List;

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
        return fieldName.equals("damagesource") ? (source != null ? EnumChatFormatting.GREEN + damagesource : EnumChatFormatting.RED + damagesource) : "" + damage;
    }
        
    @Override
    public void reward(EntityPlayerMP player) {
        if (source == null) return;
        player.attackEntityFrom(source, damage);
    }

    @Override
    public void addTooltip(List list) {
        // list.add(EnumChatFormatting.WHITE + Progression.translate("item.free"));
        // list.add(getIcon().getDisplayName() + " x" + spawnNumber);
    }
}
