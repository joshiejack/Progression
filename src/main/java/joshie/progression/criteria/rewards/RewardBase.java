package joshie.progression.criteria.rewards;

import java.util.ArrayList;
import java.util.List;

import joshie.progression.api.EventBusType;
import joshie.progression.api.ICriteria;
import joshie.progression.api.IRewardType;
import joshie.progression.gui.fields.AbstractField;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.json.Theme;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public abstract class RewardBase implements IRewardType {
    protected List<AbstractField> list = new ArrayList();
    protected ICriteria criteria;
    private String name;
    private int color;
    private boolean mustClaim = false;
    private ItemStack stack;
    
    public RewardBase(ItemStack stack, String name, int color) {
        this(name, color);
        this.stack = stack;
    }

    public RewardBase(String name, int color) {
        this.name = name;
        this.color = color;
        this.stack = new ItemStack(Blocks.stone);
    }
    
    @Override
    public ItemStack getIcon() {
        return stack;
    }
    
    @Override
    public void markCriteria(ICriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public String getUnlocalisedName() {
        return name;
    }

    @Override
    public String getLocalisedName() {
        return StatCollector.translateToLocal("progression.reward." + getUnlocalisedName());
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public EventBusType[] getEventBusTypes() {
        return new EventBusType[] { getEventBus() };
    }
    
    protected EventBusType getEventBus() {
        return EventBusType.NONE;
    }

    @Override
    public void onAdded() {}

    @Override
    public void onRemoved() {}
    
    @Override
    public Result onClicked(int mouseX, int mouseY) {
        if (MCClientHelper.canEdit()) {
            int index = 0;
            for (AbstractField t : list) {
                int color = Theme.INSTANCE.optionsFontColor;
                int yPos = 17 + (index * 8);
                if (mouseX >= 1 && mouseX <= 84) {
                    if (mouseY >= yPos && mouseY < yPos + 8) {
                        t.click();
                        return Result.ALLOW;
                    }
                }
                
                if (t.attemptClick(mouseX, mouseY)) {
                    return Result.ALLOW;
                }
                
                index++;
            }
        }

        return Result.DEFAULT;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        int index = 0;
        for (AbstractField t : list) {
            int color = Theme.INSTANCE.optionsFontColor;
            int yPos = 17 + (index * 8);
            if (MCClientHelper.canEdit()) {
                if (mouseX >= 1 && mouseX <= 84) {
                    if (mouseY >= yPos && mouseY < yPos + 8) {
                        color = Theme.INSTANCE.optionsFontColorHover;
                    }
                }
            }

            t.draw(color, yPos);
            index++;
        }
    }
}
