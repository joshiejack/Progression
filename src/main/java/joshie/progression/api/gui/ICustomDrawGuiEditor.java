package joshie.progression.api.gui;

/** Implement this on rewards, triggers, filters, conditions,
 *  if you wish to draw something special on them, other than default fields. */
public interface ICustomDrawGuiEditor {
    /** If this is true, only what you decide to draw will show up **/
    public boolean hideDefaultEditor();
    
    /** Called when clicking, return true if a click was processed **/
    public boolean mouseClicked(int mouseX, int mouseY);
    
    /** Draw your custom editor **/
    public void drawEditor(IDrawHelper helper, int renderX, int renderY, int mouseX, int mouseY);
}
