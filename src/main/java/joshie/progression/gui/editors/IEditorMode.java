package joshie.progression.gui.editors;

import joshie.progression.gui.core.GuiCore;

public interface IEditorMode {
    /** Called when initGui is, to reload any data for this gui 
     * @param fields 
     * @param features **/
    public void initData(GuiCore core);

    /** The key for this editor for saving the offset **/
    public Object getKey();
    
    /** Return the screen that comes before, for processing right click to go back **/
    public IEditorMode getPreviousGui(); 
    
    /** Mouse Click **/
    public boolean guiMouseClicked(boolean overlayvisible, int mouseX, int i, int button);
    
    /** Mouse Released **/
    public void guiMouseReleased(int mouseX, int mouseY, int button);

    /** Draw the Gui **/
    public void drawGuiForeground(boolean overlayvisible, int mouseX, int i);

    /** Key Typed **/
    public void keyTyped(char character, int key);

    /** Handle Mouse **/
    public void handleMouseInput(int mouseX, int mouseY);
}
