package joshie.crafting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import joshie.crafting.api.ICriteria;
import joshie.crafting.gui.EditorTree;
import joshie.crafting.gui.ViewerCriteria;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class Criteria implements ICriteria {
	/** All the data for this **/
	private List<Trigger> triggers = new ArrayList();
	private List<Reward> rewards = new ArrayList();
	private List<Criteria> prereqs = new ArrayList();
	private List<Criteria> conflicts = new ArrayList();
	private EditorTree treeEditor;
	private ViewerCriteria criteriaViewer;
	private int isRepeatable = 1;
	private String uniqueName;
	private String displayName;
	private boolean isVisible;
	private Tab tab;
	private ItemStack stack;

	public Criteria() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			this.treeEditor = new EditorTree(this);
			this.criteriaViewer = new ViewerCriteria(this);
		}
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public Criteria setUniqueName(String unique) {
		this.uniqueName = unique;
		return this;
	}

	public String getDisplayName() {
		return displayName;
	}

	public Criteria setDisplayName(String display) {
		this.displayName = display;
		return this;
	}

	public Criteria addTriggers(Trigger... triggers) {
		this.triggers.addAll(Arrays.asList((Trigger[]) triggers));
		return this;
	}

	public Criteria addRewards(Reward... rewards) {
		this.rewards.addAll(Arrays.asList((Reward[]) rewards));
		for (Reward reward : rewards) {
			reward.getType().onAdded();
		}

		return this;
	}

	public Criteria addRequirements(Criteria... prereqs) {
		this.prereqs.addAll(Arrays.asList((Criteria[]) prereqs));
		return this;
	}

	public Criteria addConflicts(Criteria... conflicts) {
		this.conflicts.addAll(Arrays.asList((Criteria[]) conflicts));
		return this;
	}

	public Criteria setRepeatAmount(int amount) {
		this.isRepeatable = amount;
		return this;
	}

	public Criteria setVisibility(boolean isVisible) {
		this.isVisible = isVisible;
		return this;
	}

	public Criteria setTab(Tab tab) {
		this.tab = tab;
		return this;
	}
	
	public Criteria setIcon(ItemStack stack) {
	    this.stack = stack;
	    return this;
	}

	public List<Trigger> getTriggers() {
		return triggers;
	}

	public List<Reward> getRewards() {
		return rewards;
	}

	public List<Criteria> getRequirements() {
		return prereqs;
	}

	public List<Criteria> getConflicts() {
		return conflicts;
	}

	public int getRepeatAmount() {
		return isRepeatable;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public Tab getTabID() {
		return tab;
	}
	
	public ItemStack getIcon() {
	    return stack;
	}

	public EditorTree getTreeEditor() {
		return treeEditor;
	}

	public ViewerCriteria getCriteriaViewer() {
		return criteriaViewer;
	}

	public void addTooltip(List<String> toolTip) {
		toolTip.add("Requires: " + getDisplayName());
	}
	
	   @Override
	    public int hashCode() {
	        final int prime = 31;
	        int result = 1;
	        result = prime * result
	                + ((uniqueName == null) ? 0 : uniqueName.hashCode());
	        return result;
	    }

	    @Override
	    public boolean equals(Object obj) {
	        if (this == obj)
	            return true;
	        if (obj == null)
	            return false;
	        if (getClass() != obj.getClass())
	            return false;
	        Criteria other = (Criteria) obj;
	        if (uniqueName == null) {
	            if (other.uniqueName != null)
	                return false;
	        } else if (!uniqueName.equals(other.uniqueName))
	            return false;
	        return true;
	    }
}
