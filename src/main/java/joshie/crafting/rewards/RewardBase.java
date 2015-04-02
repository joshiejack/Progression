package joshie.crafting.rewards;

import joshie.crafting.api.Bus;
import joshie.crafting.api.ICriteria;
import joshie.crafting.api.IReward;
import joshie.crafting.gui.GuiCriteriaEditor;
import joshie.crafting.helpers.ClientHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.common.eventhandler.Event.Result;

public abstract class RewardBase implements IReward {
    private static final ResourceLocation textures = new ResourceLocation("crafting", "textures/gui/textures.png");
    private String localised;
    private String typeName;
    private int color;
    protected ICriteria criteria;

    public RewardBase(String localised, int color, String typeName) {
        this.localised = localised;
        this.color = color;
        this.typeName = typeName;
    }

    @Override
    public IReward setCriteria(ICriteria criteria) {
        this.criteria = criteria;
        return this;
    }

    @Override
    public ICriteria getCriteria() {
        return this.criteria;
    }

    @Override
    public String getTypeName() {
        return typeName;
    }

    @Override
    public String getLocalisedName() {
        return localised;
    }

    public int getColor() {
        return color;
    }

    @Override
    public Bus getBusType() {
        return Bus.NONE;
    }

    @Override
    public void onAdded() {}

    @Override
    public void onRemoved() {}

    protected int xPosition;
    protected int mouseX;
    protected int mouseY;

    protected void drawText(String text, int x, int y, int color) {
        GuiCriteriaEditor.INSTANCE.selected.getCriteriaEditor().drawText(text, xPosition + x, y + 140, color);
    }

    protected void drawGradient(int x, int y, int width, int height, int color, int color2, int border) {
        GuiCriteriaEditor.INSTANCE.selected.getCriteriaEditor().drawGradient(xPosition + x, y + 140, width, height, color, color2, border);
    }

    protected void drawBox(int x, int y, int width, int height, int color, int border) {
        GuiCriteriaEditor.INSTANCE.selected.getCriteriaEditor().drawBox(xPosition + x, y + 140, width, height, color, border);
    }

    protected void drawStack(ItemStack stack, int x, int y, float scale) {
        GuiCriteriaEditor.INSTANCE.selected.getCriteriaEditor().drawStack(stack, xPosition + x, y + 140, scale);
    }
    
    protected void drawTexture(int x, int y, int u, int v, int width, int height) {
        GuiCriteriaEditor.INSTANCE.selected.getCriteriaEditor().drawTexture(xPosition + x, y + 140, u, v, width, height);
    }

    protected void draw() {

    }

    @Override
    public Result onClicked() {
        if (this.mouseX >= 88 && this.mouseX <= 95 && this.mouseY >= 4 && this.mouseY <= 14) {
            return Result.DENY; //Delete this reward
        }

        return clicked();
    }

    public Result clicked() {
        return Result.DEFAULT;
    }

    @Override
    public void draw(int mouseX, int mouseY, int xPos) {
        this.mouseX = mouseX - xPosition;
        this.mouseY = mouseY - 140;
        this.xPosition = xPos + 6;

        drawGradient(1, 2, 99, 15, getColor(), 0xFF222222, 0xFF000000);
        drawText(getLocalisedName(), 6, 6, 0xFFFFFFFF);
        int xXcoord = 0;
        if (this.mouseX >= 87 && this.mouseX <= 97 && this.mouseY >= 4 && this.mouseY <= 14) {
            xXcoord = 11;
        }

        ClientHelper.getMinecraft().getTextureManager().bindTexture(textures);
        drawTexture(87, 4, xXcoord, 195, 11, 11);
        draw();
    }
}
