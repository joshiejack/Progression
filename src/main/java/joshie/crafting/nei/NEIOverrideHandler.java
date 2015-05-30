package joshie.crafting.nei;

import java.util.Collection;

import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICriteria;
import joshie.crafting.api.crafting.CraftingEvent.CraftingType;
import joshie.crafting.api.crafting.ICrafter;
import joshie.crafting.gui.EditorTicker;
import joshie.crafting.gui.GuiCriteriaEditor;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.json.Options;
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
            ICrafter crafter = CraftingAPI.crafting.getCrafterFromPlayer(ClientHelper.getPlayer());
            if (!crafter.canCraftItem(CraftingType.CRAFTING, result)) {
                Collection<ICriteria> requirements = CraftingAPI.crafting.getCraftingCriteria(CraftingType.CRAFTING, result);
                if (requirements.size() > 0) {
                    for (ICriteria c : requirements) {
                        GuiCriteriaEditor.INSTANCE.selected = c;
                        break;
                    }

                    EditorTicker.OVERRIDE_TICK = 1;
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
