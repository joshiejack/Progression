package joshie.crafting;

import joshie.crafting.gui.GuiCriteriaEditor;
import joshie.crafting.gui.GuiTreeEditorDisplay;
import joshie.crafting.gui.GuiTreeEditorEdit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class CraftingGUIHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    	return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == 1) return GuiCriteriaEditor.INSTANCE;
        if (CraftingMod.options.editor) {
            return GuiTreeEditorEdit.INSTANCE;
        } else if (CraftingMod.options.display) {
            return GuiTreeEditorDisplay.INSTANCE;
        } else return null;
    }
}