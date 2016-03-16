package joshie.progression.handlers;

import joshie.progression.gui.GuiTreeEditor;
import joshie.progression.gui.newversion.GuiConditionEditor;
import joshie.progression.gui.newversion.GuiCriteriaEditor;
import joshie.progression.gui.newversion.GuiItemFilterEditor;
import joshie.progression.lib.GuiIDs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GUIHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == GuiIDs.CRITERIA) return GuiCriteriaEditor.INSTANCE;
        else if (ID == GuiIDs.CONDITION) return GuiConditionEditor.INSTANCE;
        else if (ID == GuiIDs.ITEM) return GuiItemFilterEditor.INSTANCE;
        else if (ID == GuiIDs.TREE) return GuiTreeEditor.INSTANCE;
        else return null;
    }
}