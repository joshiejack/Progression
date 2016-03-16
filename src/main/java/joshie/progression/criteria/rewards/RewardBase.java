package joshie.progression.criteria.rewards;

import java.util.ArrayList;
import java.util.List;

import joshie.progression.Progression;
import joshie.progression.api.ICriteria;
import joshie.progression.api.IField;
import joshie.progression.api.IRewardType;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public abstract class RewardBase implements IRewardType {
    protected List<IField> list = new ArrayList();
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
    public void setCriteria(ICriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public String getUnlocalisedName() {
        return name;
    }

    @Override
    public String getLocalisedName() {
        return Progression.translate("reward." + getUnlocalisedName());
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public void onAdded() {}

    @Override
    public void onRemoved() {}

    @Override
    public void updateDraw() {}

    @Override
    public String getDescription() {
        return "MISSING DESCRIPTION";
    }

    @Override
    public void addTooltip(List list) {}
}
