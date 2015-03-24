package joshie.crafting.trigger;

import joshie.crafting.api.IHasUniqueName;
import joshie.crafting.api.ITrigger;
import joshie.crafting.api.ITriggerType;

public abstract class TriggerBase implements ITrigger, ITriggerType {
	private String uniqueName;
	private String typeName;
	
	public TriggerBase(String typeName) {
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
}
