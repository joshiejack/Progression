package joshie.progression.nei;

import java.util.Collection;

import joshie.progression.api.ICriteria;
import joshie.progression.crafting.Crafter;
import joshie.progression.crafting.CraftingRegistry;
import joshie.progression.crafting.CraftingType;
import joshie.progression.criteria.Criteria;
import joshie.progression.gui.EditorTicker;
import joshie.progression.gui.GuiCriteriaEditor;
import joshie.progression.helpers.ClientHelper;
import net.minecraft.item.ItemStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class NEIOverrideHandler extends TemplateRecipeHandler {
    @Override
    public TemplateRecipeHandler newInstance() {
        return super.newInstance();
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        Crafter crafter = CraftingRegistry.getCrafterFromPlayer(ClientHelper.getPlayer());
        if (!crafter.canCraftItem(CraftingType.CRAFTING, result)) {
            Collection<ICriteria> requirements = CraftingRegistry.getCraftingCriteria(CraftingType.CRAFTING, result);
            if (requirements.size() > 0) {
                for (ICriteria c : requirements) {
                    GuiCriteriaEditor.INSTANCE.selected = (Criteria) c;
                    break;
                }

                EditorTicker.OVERRIDE_TICK = 1;
            }
        }
    }

    @Override
    public String getRecipeName() {
        return "crafting.override";
    }

    @Override
    public String getGuiTexture() {
        return "null";
    }
}
