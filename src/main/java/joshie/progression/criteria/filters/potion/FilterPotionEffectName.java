package joshie.progression.criteria.filters.potion;

import com.google.gson.JsonObject;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.ISpecialJSON;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.Arrays;
import java.util.Collection;

@ProgressionRule(name="potioneffect", color=0xFFFF73FF)
public class FilterPotionEffectName extends FilterPotionBase implements ISpecialJSON {
    public String name = "minecraft:speed";

    @Override
    public boolean matches(PotionEffect effect) {
        return effect.getPotion().getRegistryName().toString().equals(name);
    }

    @Override
    public Collection<PotionEffect> getRandomEffects() {
        return Arrays.asList(new PotionEffect(Potion.getPotionFromResourceLocation(name), 200, 0));
    }

    @Override
    public boolean onlySpecial() {
        return false;
    }

    @Override
    public void readFromJSON(JsonObject object) {
        if (object.get("effectID") != null) {
            try {
                int effectID = object.get("effectID").getAsInt();
                Potion potion = Potion.getPotionById(effectID);
                if (potion != null) {
                    name = potion.getRegistryName().toString();
                }
            } catch (Exception e) {}
        }
    }

    @Override
    public void writeToJSON(JsonObject object) {}
}
