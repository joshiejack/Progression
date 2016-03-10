package joshie.progression.gui.newversion.overlays;

public interface IBarProvider {
	public int getColorForBar(BarColorType type);
	
	public static enum BarColorType {
		BAR1_GRADIENT1, BAR1_GRADIENT2, BAR1_BORDER, BAR1_UNDERLINE, BAR1_FONT,
		BAR2_GRADIENT1, BAR2_GRADIENT2, BAR2_BORDER, BAR2_UNDERLINE, BAR2_FONT;
	}
}
