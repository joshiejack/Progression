package joshie.crafting.json;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import joshie.crafting.helpers.StackHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.google.gson.JsonObject;

public class DefaultSettings {
    public Set<DataTab> tabs = new HashSet();

    public DefaultSettings setDefaults() {
        ArrayList<DataGeneric> rewardsNewCondition = new ArrayList();
        ArrayList<DataTrigger> triggersNewCondition = new ArrayList();
        ArrayList<DataGeneric> rewardsNamedCondition = new ArrayList();
        ArrayList<DataTrigger> triggersNamedCondition = new ArrayList();
        ArrayList<DataGeneric> rewardsGoldenPig = new ArrayList();
        ArrayList<DataTrigger> triggersGoldenPig = new ArrayList();
        ArrayList<DataGeneric> rewardsLapis = new ArrayList();
        ArrayList<DataTrigger> triggersLapis = new ArrayList();
        ArrayList<DataGeneric> conditions = new ArrayList();
        ArrayList<DataGeneric> nightCondition = new ArrayList();

        //Conditions
        JsonObject night = new JsonObject();
        nightCondition.add(new DataGeneric("daytime", night));

        //Triggers
        JsonObject object = new JsonObject();
        ItemStack stack = new ItemStack(Blocks.bookshelf);
        String serial = StackHelper.getStringFromStack(stack);
        object.addProperty("item", serial);
        object.addProperty("amount", 5);
        triggersNewCondition.add(new DataTrigger("breakBlock", object, conditions));
        JsonObject iron = new JsonObject();
        iron.addProperty("researchName", "Iron Heights");
        triggersNamedCondition.add(new DataTrigger("research", iron, conditions));
        JsonObject pig = new JsonObject();
        pig.addProperty("entity", "Pig");
        triggersGoldenPig.add(new DataTrigger("kill", pig, nightCondition));
        JsonObject crafting = new JsonObject();
        crafting.addProperty("item", "minecraft:diamond_block");
        triggersLapis.add(new DataTrigger("crafting", crafting, conditions));

        //Rewards
        JsonObject speed = new JsonObject();
        speed.addProperty("speed", 0.1F);
        rewardsNewCondition.add(new DataGeneric("speed", speed));
        JsonObject speed2 = new JsonObject();
        speed2.addProperty("speed", 0.5F);
        rewardsNamedCondition.add(new DataGeneric("speed", speed2));

        JsonObject iron2 = new JsonObject();
        iron2.addProperty("item", "minecraft:iron_block");
        rewardsNamedCondition.add(new DataGeneric("crafting", iron2));
        JsonObject gold = new JsonObject();
        gold.addProperty("item", "minecraft:gold_block");
        rewardsGoldenPig.add(new DataGeneric("crafting", gold));
        JsonObject lapis = new JsonObject();
        lapis.addProperty("item", "minecraft:lapis_block");
        rewardsNewCondition.add(new DataGeneric("crafting", lapis));
        JsonObject lapis2 = new JsonObject();
        lapis2.addProperty("item", "minecraft:lapis_block");
        rewardsLapis.add(new DataGeneric("crafting", lapis2));

        List<DataCriteria> data = new ArrayList();
        //Criteria
        data.add(new DataCriteria("NEW CONDITION", "A New Condition", triggersNewCondition, rewardsNewCondition, new String[] { "GoldenPig" }, new String[] {}, 55, 0, true));
        data.add(new DataCriteria("NamedCondition", "A Named Condition", triggersNamedCondition, rewardsNamedCondition, new String[] {}, new String[] {}, 0, 55, true));
        data.add(new DataCriteria("GoldenPig", "Golden Pig", triggersGoldenPig, rewardsGoldenPig, new String[] {}, new String[] {}, 0, 0, true));
        data.add(new DataCriteria("EnableLapis", "Enable Lapis", triggersLapis, rewardsLapis, new String[] {}, new String[] {}, 0, 110, true));
        DataTab defaultTab = new DataTab("DEFAULT", "Default", data, true, new ItemStack(Items.book));
        tabs.add(defaultTab);
        return this;
    }
}
