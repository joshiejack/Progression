package joshie.crafting.conditions;

import joshie.crafting.api.ICondition;
import joshie.crafting.api.ICriteria;

public abstract class ConditionBase implements ICondition {
	private String typeName;
	private boolean inverted = false;
	private ICriteria criteria;
	
	public ConditionBase(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public String getTypeName() {
		return typeName;
	}
	
	@Override
    public ICondition setCriteria(ICriteria criteria) {
        this.criteria = criteria;
        return this;
    }

    @Override
    public ICriteria getCriteria() {
        return this.criteria;
    }

	@Override
	public ICondition setInversion(boolean inverted) {
		this.inverted = inverted;
		return this;
	}

	@Override
	public boolean isInverted() {
		return inverted;
	}
}
