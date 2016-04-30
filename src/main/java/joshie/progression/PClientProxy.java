package joshie.progression;

import joshie.progression.ItemProgressionRenderer.ProgressionOverride;
import joshie.progression.handlers.APICache;
import joshie.progression.handlers.TemplateHandler;
import joshie.progression.helpers.ChatHelper;
import joshie.progression.helpers.RenderItemHelper;
import joshie.progression.json.Options;
import joshie.progression.lib.GuiIDs;
import joshie.progression.lib.PInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import static joshie.progression.Progression.translate;
import static joshie.progression.gui.core.GuiList.CORE;
import static joshie.progression.gui.core.GuiList.GROUP_EDITOR;
import static net.minecraft.util.text.TextFormatting.GOLD;

public class PClientProxy extends PCommonProxy {
    public static final ModelResourceLocation criteria = new ModelResourceLocation(new ResourceLocation(PInfo.MODPATH, "item"), "inventory");
    public static boolean bookLocked = true; //You can't edit me
    public static boolean isSaver = false;

    @Override
    public void initClient() {
        MinecraftForge.EVENT_BUS.register(new ItemProgressionRenderer());
        APICache.resetAPIHandler(true); //Reset the cache once we've received the length
    }

    private ModelResourceLocation getLocation(String name) {
        return new ModelResourceLocation(new ResourceLocation(PInfo.MODPATH, name), "inventory");
    }

    @Override
    public void registerRendering() {
        RenderItemHelper.register(Progression.item, 0, criteria);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(ProgressionOverride.INSTANCE, Progression.item);
        for (ItemProgression.ItemMeta meta: ItemProgression.ItemMeta.values()) {
            if (meta == ItemProgression.ItemMeta.criteria) continue;
            RenderItemHelper.register(Progression.item, meta.ordinal(), getLocation(meta.name()));
        }

        //Load Templates
        TemplateHandler.init();
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == GuiIDs.EDITOR) {
            if (bookLocked) {
                if (Options.editor) ChatHelper.displayChat(GOLD + translate("message.data"));
                else ChatHelper.displayChat(GOLD + translate("message.disabled"));
            } else return CORE.setEditor(CORE.lastGui);
        }  else if (ID == GuiIDs.GROUP) return CORE.setEditor(GROUP_EDITOR);


        return null;
    }
}