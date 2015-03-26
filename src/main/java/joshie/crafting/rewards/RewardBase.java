package joshie.crafting.rewards;

import joshie.crafting.api.Bus;
import joshie.crafting.api.ICriteria;
import joshie.crafting.api.IHasUniqueName;
import joshie.crafting.api.IReward;
import joshie.crafting.gui.GuiCriteriaEditor;

public abstract class RewardBase implements IReward {
	private String uniqueName;
	private String typeName;
	
	public RewardBase(String typeName) {
		this.typeName = typeName;
	}
	
	@Override
	public Bus getBusType() {
		return Bus.NONE;
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
	
	protected void drawText(String text, int x, int y, int color) {
        GuiCriteriaEditor.INSTANCE.selected.getCriteriaEditor().drawText(text, x, y, color);
    }

    @Override
    public void draw(int xPos) {
        String tName = getUniqueName();
        String tType = getTypeName();
        drawText("Name: " + tName, 9 + xPos, 139, 0xFFFFFFFF);
        drawText("Type: " + tType, 9 + xPos, 149, 0xFFFFFFFF);
    }
}
