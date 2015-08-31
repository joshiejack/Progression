package joshie.progression.nei;

import java.util.Collection;

import joshie.progression.api.ICriteria;
import joshie.progression.crafting.ActionType;
import joshie.progression.crafting.Crafter;
import joshie.progression.crafting.CraftingRegistry;
import joshie.progression.criteria.Criteria;
import joshie.progression.gui.GuiCriteriaEditor;
import joshie.progression.gui.base.SaveTicker;
import joshie.progression.helpers.ClientHelper;
import joshie.progression.json.Options;
import net.minecraft.item.ItemStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class NEIOverrideHandler extends TemplateRecipeHandler {
    @Override
    public TemplateRecipeHandler newInstance() {
        return super.newInstance();
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (Options.settings.displayRequirementsOnNEIClick) {
            Crafter crafter = CraftingRegistry.getCrafterFromPlayer(ClientHelper.getPlayer());
            if (!crafter.canCraftItem(ActionType.CRAFTING, result)) {
                Collection<ICriteria> requirements = CraftingRegistry.getCraftingCriteria(ActionType.CRAFTING, result);
                if (requirements.size() > 0) {
                    for (ICriteria c : requirements) {
                        GuiCriteriaEditor.INSTANCE.selected = (Criteria) c;
                        break;
                    }

                    SaveTicker.OVERRIDE_TICK = 1;
                }
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
