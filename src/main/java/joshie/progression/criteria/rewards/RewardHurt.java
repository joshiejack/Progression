package joshie.progression.criteria.rewards;

import joshie.progression.Progression;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.util.HashMap;

@ProgressionRule(name="hurt", color=0xFFE599FF, meta="attackPlayer")
public class RewardHurt extends RewardBase implements IInit, ICustomDescription, ICustomWidth, IGetterCallback {
    public static final HashMap<String, DamageSource> sources = new HashMap();
    private DamageSource source;
    public String damageSource = "magic";
    public float damage = 1F;

    @Override
    public void init(boolean isClient) {
        if (damageSource.equals("magic")) source = DamageSource.magic;
        else {
            source = sources.get(damageSource);
        }
    }

    @Override
    public String getDescription() {
        if (source != null) {
            String s = EnumChatFormatting.GREEN + WordUtils.capitalizeFully(StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(source.damageType), ' '));
            return Progression.format(getProvider().getUnlocalisedName() + ".description", EnumChatFormatting.RED + "" + damage + EnumChatFormatting.WHITE, s);
        } else return "Incorrectly setup damage source";
    }

    @Override
    public int getWidth(DisplayMode mode) {
        return mode == DisplayMode.EDIT ? 85 : 80;
    }
    
    @Override
    public String getField(String fieldName) {
        return fieldName.equals("damageSource") ? (source != null ? EnumChatFormatting.GREEN + damageSource : EnumChatFormatting.RED + damageSource) : "" + damage;
    }
        
    @Override
    public void reward(EntityPlayerMP player) {
        if (source == null) return;
        player.attackEntityFrom(source, damage);
    }
}
