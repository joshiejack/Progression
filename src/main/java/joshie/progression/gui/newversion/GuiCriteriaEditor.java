package joshie.progression.gui.newversion;

import java.util.ArrayList;

import joshie.progression.criteria.Criteria;
import joshie.progression.gui.newversion.overlays.FeatureBarsX2;
import joshie.progression.gui.newversion.overlays.FeatureDrawable;
import joshie.progression.gui.newversion.overlays.FeatureItemSelector;
import joshie.progression.gui.newversion.overlays.IBarProvider;

public class GuiCriteriaEditor extends GuiCore implements IBarProvider {
	public static final GuiCriteriaEditor INSTANCE = new GuiCriteriaEditor();
	private Criteria criteria;

	private GuiCriteriaEditor() {}
	
	public void setCriteria(Criteria criteria) {
		this.criteria = criteria;
	}
	
	@Override
	public void addFeatures() {
		offsetX = 0; //Reset the offsetX
		features.add(new FeatureBarsX2(this, "requirements", "results"));
		features.add(new FeatureDrawable(new ArrayList(criteria.triggers), 45));
		features.add(new FeatureDrawable(new ArrayList(criteria.rewards), 140));
		features.add(FeatureItemSelector.INSTANCE); //Add the item selector
	}

	@Override
	public int getColorForBar(BarColorType type) {
		switch (type) {
		case BAR1_GRADIENT1:
			return theme.triggerBoxGradient1;
		case BAR1_GRADIENT2:
			return theme.triggerBoxGradient2;
		case BAR1_BORDER:
			return theme.triggerBoxUnderline1;
		case BAR1_FONT:
			return theme.triggerBoxFont;
		case BAR1_UNDERLINE:
			return theme.triggerBoxUnderline1;
		case BAR2_GRADIENT1:
			return theme.rewardBoxGradient1;
		case BAR2_GRADIENT2:
			return theme.rewardBoxGradient2;
		case BAR2_BORDER:
			return theme.rewardBoxBorder;
		case BAR2_FONT:
			return theme.rewardBoxFont;
		default:
			return 0;
		}
	}
}
