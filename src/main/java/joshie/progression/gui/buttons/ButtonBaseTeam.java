package joshie.progression.gui.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

import static joshie.progression.gui.core.GuiList.CORE;

public abstract class ButtonBaseTeam extends GuiButton {
    public ButtonBaseTeam(String text, int x, int y) {
        super(0, x, y, text.length() * 7, 20, text);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        mouseY = mouseY + CORE.screenTop;
        if (super.mousePressed(mc, mouseX, mouseY)) {
            onClicked();
            return true;
        } else {
            onNotClicked();
            return false;
        }
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        mouseY = mouseY + CORE.screenTop;
        FontRenderer fontrenderer = mc.fontRendererObj;
        mc.getTextureManager().bindTexture(buttonTextures);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
        int i = this.getHoverState(this.hovered);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.blendFunc(770, 771);
        this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + i * 20, this.width / 2, this.height);
        this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
        this.mouseDragged(mc, mouseX, mouseY);
        int j = 14737632;

        if (packedFGColour != 0)
        {
            j = packedFGColour;
        }
        else
        if (!this.enabled)
        {
            j = 10526880;
        }
        else if (this.hovered)
        {
            j = 16777120;
        }

        this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
        if (this.hovered) {
            addTooltip();
        }
    }

    public void addTooltip() {

    }

    public abstract void onClicked();

    public void onNotClicked() {}
}
