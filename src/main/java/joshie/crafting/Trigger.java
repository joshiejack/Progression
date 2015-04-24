package joshie.crafting;

import java.util.ArrayList;
import java.util.List;

import joshie.crafting.api.DrawHelper;
import joshie.crafting.api.DrawHelper.IDrawHelper;
import joshie.crafting.api.ICondition;
import joshie.crafting.api.IConditionEditor;
import joshie.crafting.api.ITriggerType;
import joshie.crafting.gui.EditorCondition;
import joshie.crafting.gui.GuiDrawHelper;
import joshie.crafting.gui.GuiTriggerEditor;
import joshie.crafting.gui.SelectTextEdit;
import joshie.crafting.gui.SelectTextEdit.ITextEditable;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.json.Theme;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.relauncher.Side;

public class Trigger {
    private static final ResourceLocation textures = new ResourceLocation("crafting", "textures/gui/textures.png");
    protected static final Theme theme = Theme.INSTANCE;
    private List<ICondition> conditions = new ArrayList();
    private Criteria criteria;
    private IConditionEditor editor;
    private ITriggerType triggerType;

    public Trigger(ITriggerType triggerType) {
        this.triggerType = triggerType;
        //Don't load the editor server side
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            this.editor = new EditorCondition(this);
        }
    }
    
    public ITriggerType getType() {
        return triggerType;
    }

    public void addCondition(ICondition condition) {
        conditions.add(condition);
    }

    public IConditionEditor getConditionEditor() {
        return editor;
    }

    public Trigger setCriteria(Criteria criteria) {
        this.criteria = criteria;
        return this;
    }

    public Criteria getCriteria() {
        return this.criteria;
    }

    public int getInternalID() {
        for (int id = 0; id < getCriteria().getTriggers().size(); id++) {
            Trigger aTrigger = getCriteria().getTriggers().get(id);
            if (aTrigger.equals(this))
                return id;
        }

        return 0;
    }

    public Trigger setConditions(ICondition[] conditions) {
        for (ICondition condition : conditions) {
            this.conditions.add(condition);
        }

        return this;
    }

    public List<ICondition> getConditions() {
        return conditions;
    }

    protected int mouseX;
    protected int mouseY;
    protected int xPosition;

    protected String getText(ITextEditable editable) {
        return SelectTextEdit.INSTANCE.getText(editable);
    }

    public Result onClicked() {
        if (ClientHelper.canEdit()) {
            if (this.mouseX >= 88 && this.mouseX <= 95 && this.mouseY >= 4 && this.mouseY <= 14) {
                return Result.DENY; // Delete this trigger
            }
        }

        if (ClientHelper.canEdit() || this.getConditions().size() > 0) {
            if (this.mouseX >= 2 && this.mouseX <= 87) {
                if (this.mouseY >= 66 && this.mouseY <= 77) {

                    GuiTriggerEditor.INSTANCE.trigger = this;
                    ClientHelper.getPlayer().openGui(CraftingMod.instance, 2,  null, 0, 0, 0);
                    return Result.ALLOW;
                }
            }
        }

        return ClientHelper.canEdit() ? clicked() : Result.DEFAULT;
    }

    public Result clicked() {
        return triggerType.onClicked(mouseX, mouseY);
    }

    public void draw(int mouseX, int mouseY, int xPos) {
        this.mouseX = mouseX - xPosition;
        this.mouseY = mouseY - 45;
        this.xPosition = xPos + 6;
        IDrawHelper helper = DrawHelper.triggerDraw;
        
        GuiDrawHelper.TriggerDrawHelper.INSTANCE.tick(mouseX, mouseY, xPosition);
        helper.drawGradient(1, 2, 99, 15, getType().getColor(), theme.triggerGradient1, theme.triggerGradient2);
        helper.drawText(getType().getLocalisedName(), 6, 6, theme.triggerFontColor);

        if (ClientHelper.canEdit()) {
            int xXcoord = 0;
            if (this.mouseX >= 87 && this.mouseX <= 97 && this.mouseY >= 4 && this.mouseY <= 14) {
                xXcoord = 11;
            }

            ClientHelper.getMinecraft().getTextureManager().bindTexture(textures);
            helper.drawTexture(87, 4, xXcoord, 195, 11, 11);
        }

        triggerType.draw(mouseX, mouseY);

        int color = theme.blackBarBorder;
        if (this.mouseX >= 2 && this.mouseX <= 87) {
            if (this.mouseY >= 66 && this.mouseY <= 77) {
                color = theme.blackBarFontColor;
            }
        }

        if (ClientHelper.canEdit()) {
            helper.drawGradient(2, 66, 85, 11, color, theme.blackBarGradient1, theme.blackBarGradient2);
            helper.drawText("Condition Editor", 6, 67, theme.blackBarFontColor);
        } else if (this.getConditions().size() > 0) {
            helper.drawGradient(2, 66, 85, 11, color, theme.blackBarGradient1, theme.blackBarGradient2);
            helper.drawText("Condition Viewer", 6, 67, theme.blackBarFontColor);
        }
    }

    /** A whole bunch of convenience methods **/

    // Shorthand
    protected Block asBlock(Object[] object) {
        return asBlock(object, 0);
    }

    // Normalhand
    protected Block asBlock(Object[] object, int index) {
        return (Block) object[index];
    }

    // Shorthand
    protected String asString(Object[] object) {
        return asString(object, 0);
    }

    // Normalhand
    protected String asString(Object[] object, int index) {
        return asString(object, 0, "");
    }

    // Longhand
    protected String asString(Object[] object, int index, String theDefault) {
        if (object != null) {
            return (String) object[index];
        } else
            return theDefault;
    }

    // Shorthand
    protected int asInt(Object[] object) {
        return asInt(object, 0);
    }

    // Normalhand
    protected int asInt(Object[] object, int index) {
        return asInt(object, index, 0);
    }

    // Longhand
    protected int asInt(Object[] object, int index, int theDefault) {
        if (object != null) {
            return (Integer) object[index];
        } else
            return theDefault;
    }

    // Shorthand
    protected boolean asBoolean(Object[] object) {
        return asBoolean(object, 0);
    }

    // Normalhand
    protected boolean asBoolean(Object[] object, int index) {
        return asBoolean(object, index, true);
    }

    // Longhand
    protected boolean asBoolean(Object[] object, int index, boolean theDefault) {
        if (object != null) {
            return (Boolean) object[index];
        } else
            return theDefault;
    }

    // Shorthand
    protected ItemStack asStack(Object[] object) {
        return asStack(object, 0);
    }

    // Normalhand
    protected ItemStack asStack(Object[] object, int index) {
        return (ItemStack) object[index];
    }
}
