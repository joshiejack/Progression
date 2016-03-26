package joshie.progression.plugins.enchiridion.features;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.gui.IToolbarButton;
import joshie.progression.lib.ProgressionInfo;
import net.minecraft.util.ResourceLocation;

public abstract class ButtonAbstract implements IToolbarButton {
    protected ResourceLocation dflt;
    protected ResourceLocation hover;
    protected String translate;
    protected String name;

    public ButtonAbstract(String name) {
        dflt = new ResourceLocation(ProgressionInfo.BOOKPATH + name + "_dftl.png");
        hover = new ResourceLocation(ProgressionInfo.BOOKPATH + name + "_hover.png");
        translate = "button." + name;
        this.name = name;
    }

    @Override
    public boolean isLeftAligned() {
        return true;
    }

    @Override
    public ResourceLocation getResource() {
        return dflt;
    }

    @Override
    public ResourceLocation getHoverResource() {
        return hover;
    }

    @Override
    public String getTooltip() {
        return Enchiridion.translate(translate);
    }
}
