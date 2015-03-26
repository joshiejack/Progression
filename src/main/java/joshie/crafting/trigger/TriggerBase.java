package joshie.crafting.trigger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import joshie.crafting.api.Bus;
import joshie.crafting.api.ICondition;
import joshie.crafting.api.IHasUniqueName;
import joshie.crafting.api.ITrigger;
import joshie.crafting.api.ITriggerData;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public abstract class TriggerBase implements ITrigger {
	private List<ICondition> conditions = new ArrayList();
	private String uniqueName;
	private String typeName;
	protected int amount = 1;
	
	public TriggerBase(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public Bus getBusType() {
		return Bus.FORGE;
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
	public ITrigger setConditions(ICondition[] conditions) {
		for (ICondition condition: conditions) {
			this.conditions.add(condition);
		}
		
		return this;
	}

	@Override
	public List<ICondition> getConditions() {
		return conditions;
	}
	
	@Override
	public void onFired(UUID uuid, ITriggerData triggerData, Object... data) {
	    onFired(triggerData, data);
	}
	
	public void onFired(ITriggerData triggerData, Object... data) {}

    /** A whole bunch of convenience methods **/
	
	//Shorthand
	protected Block asBlock(Object[] object) {
		return asBlock(object, 0);
	}
		
	//Normalhand
	protected Block asBlock(Object[] object, int index) {
		return (Block) object[index];
	}	
	
	//Shorthand
	protected String asString(Object[] object) {
		return asString(object, 0);
	}
	
	//Normalhand
	protected String asString(Object[] object, int index) {
		return asString(object, 0, "");
	}
	
	//Longhand
	protected String asString(Object[] object, int index, String theDefault) {
		if (object != null) {
			return (String) object[index];
		} else return theDefault;
	}
	
	//Shorthand
	protected int asInt(Object[] object) {
		return asInt(object, 0);
	}
	
	//Normalhand
	protected int asInt(Object[] object, int index) {
		return asInt(object, index, 0);
	}
	
	//Longhand
	protected int asInt(Object[] object, int index, int theDefault) {
		if (object != null) {
			return (Integer) object[index];
		} else return theDefault;
	}
	
	//Shorthand
	protected boolean asBoolean(Object[] object) {
		return asBoolean(object, 0);
	}
		
	//Normalhand
	protected boolean asBoolean(Object[] object, int index) {
		return asBoolean(object, index, true);
	}
		
	//Longhand
	protected boolean asBoolean(Object[] object, int index, boolean theDefault) {
		if (object != null) {
			return (Boolean) object[index];
		} else return theDefault;
	}
	
	//Shorthand
	protected ItemStack asStack(Object[] object) {
		return asStack(object, 0);
	}
		
	//Normalhand
	protected ItemStack asStack(Object[] object, int index) {
		return (ItemStack) object[index];
	}
}
