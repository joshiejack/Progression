package joshie.progression.criteria.filters.potion;

import com.google.gson.JsonObject;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.ISpecialJSON;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.Arrays;
import java.util.Collection;

@ProgressionRule(name="potioneffect", color=0xFFFF73FF)
public class FilterPotionEffectID extends FilterPotionBase implements ISpecialJSON {
    public int effectID = 1;

    @Override
    public boolean matches(PotionEffect effect) {
        return effect.getPotionID() == effectID;
    }

    @Override
    public Collection<PotionEffect> getRandomEffects() {
        return Arrays.asList(new PotionEffect(effectID, 200, 0));
    }

    @Override
    public boolean onlySpecial() {
        return false;
    }

    @Override
    public void readFromJSON(JsonObject object) {
        if (object.get("name") != null) {
            try {
                String name = object.get("name").getAsString();
                effectID = Potion.getPotionFromResourceLocation(name).id;
            } catch (Exception e) {}
        }
    }

    @Override
    public void writeToJSON(JsonObject object) {}
}
