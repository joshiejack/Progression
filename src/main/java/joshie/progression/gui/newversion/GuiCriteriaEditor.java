package joshie.progression.gui.newversion;

import java.util.ArrayList;
import java.util.HashMap;

import joshie.progression.criteria.Criteria;
import joshie.progression.gui.newversion.overlays.FeatureBarsX2;
import joshie.progression.gui.newversion.overlays.FeatureDrawable;
import joshie.progression.gui.newversion.overlays.FeatureItemSelector;
import joshie.progression.gui.newversion.overlays.FeatureNewReward;
import joshie.progression.gui.newversion.overlays.FeatureNewTrigger;
import joshie.progression.gui.newversion.overlays.IBarProvider;

public class GuiCriteriaEditor extends GuiCore implements IBarProvider {
	public static final GuiCriteriaEditor INSTANCE = new GuiCriteriaEditor();
	public Criteria criteria;

	private GuiCriteriaEditor() {}
	
	public void setCriteria(Criteria criteria) {
		this.criteria = criteria;
	}
	
	@Override
	public Object getKey() {
	    return criteria;
	}
	
	@Override
	public void addFeatures() {
		features.add(new FeatureBarsX2(this, "requirements", "results"));
		features.add(new FeatureDrawable(new ArrayList(criteria.triggers), 45, 201, 201, 64, 119, FeatureNewTrigger.INSTANCE));
		features.add(new FeatureDrawable(new ArrayList(criteria.rewards), 140, 0, 55, 201, 201, FeatureNewReward.INSTANCE));
		features.add(FeatureItemSelector.INSTANCE); //Add the item selector
		features.add(FeatureNewTrigger.INSTANCE); //Add new trigger popup
		features.add(FeatureNewReward.INSTANCE);  //Add new reward popup
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
