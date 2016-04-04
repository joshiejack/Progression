package joshie.progression;

import joshie.progression.gui.core.GuiCore;
import joshie.progression.gui.editors.GuiGroupEditor;
import joshie.progression.helpers.RenderItemHelper;
import joshie.progression.lib.GuiIDs;
import joshie.progression.lib.ProgressionInfo;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class PClientProxy extends PCommonProxy {
    public static final ModelResourceLocation criteria = new ModelResourceLocation(new ResourceLocation(ProgressionInfo.MODPATH, "item"), "inventory");

    @Override
    public void initClient() {
        MinecraftForge.EVENT_BUS.register(new ItemProgressionRenderer());
    }

    private ModelResourceLocation getLocation(String name) {
        return new ModelResourceLocation(new ResourceLocation(ProgressionInfo.MODPATH, name), "inventory");
    }

    @Override
    public void registerRendering() {
        RenderItemHelper.register(Progression.item, 0, criteria);
        for (ItemProgression.ItemMeta meta: ItemProgression.ItemMeta.values()) {
            if (meta == ItemProgression.ItemMeta.criteria) continue;
            RenderItemHelper.register(Progression.item, meta.ordinal(), getLocation(meta.name()));
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == GuiIDs.EDITOR) return GuiCore.INSTANCE.setEditor(GuiCore.INSTANCE.lastGui);
        else if (ID == GuiIDs.GROUP) return GuiCore.INSTANCE.setEditor(GuiGroupEditor.INSTANCE);
        else return null;
    }
}