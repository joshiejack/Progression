package joshie.progression.criteria.rewards;

import joshie.progression.Progression;
import joshie.progression.api.criteria.IProgressionCriteria;
import joshie.progression.api.criteria.IProgressionReward;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.UUID;

public abstract class RewardBase implements IProgressionReward {
    protected IProgressionCriteria criteria;
    private String name;
    private int color;
    private ItemStack stack;
    private UUID uuid;

    //Whether users must claim this reward or not
    public boolean mustClaim = false;

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
    public UUID getUniqueID() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }

        return uuid;
    }

    @Override
    public ItemStack getIcon() {
        return stack;
    }

    @Override
    public void setCriteria(IProgressionCriteria criteria, UUID uuid) {
        this.criteria = criteria;
        this.uuid = uuid;
    }

    @Override
    public IProgressionCriteria getCriteria() {
        return criteria;
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
    public boolean isAutomatic() {
        return !mustClaim;
    }

    @Override
    public boolean shouldRunOnce() {
        return false;
    }
    
    @Override
    public void reward(EntityPlayerMP player) {}

    @Override
    public void updateDraw() {}

    @Override
    public String getDescription() {
        return "MISSING DESCRIPTION";
    }

    @Override
    public void addTooltip(List list) {}
}
