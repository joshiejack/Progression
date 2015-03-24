package joshie.crafting.rewards;

import joshie.crafting.api.ICriteria;
import joshie.crafting.api.IHasUniqueName;
import joshie.crafting.api.IReward;
import joshie.crafting.api.IRewardType;

public abstract class RewardBase implements IReward, IRewardType {
	private String uniqueName;
	private String typeName;
	
	public RewardBase(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public String getTypeName() {
		return typeName;
	}
	
	@Override
	public String getUniqueName() {
		return uniqueName;
	}

	@Override
	public IHasUniqueName setUniqueName(String unique) {
		this.uniqueName = unique;
		return this;
	}
	
	@Override
	public void onAdded(ICriteria criteria) {}
}
