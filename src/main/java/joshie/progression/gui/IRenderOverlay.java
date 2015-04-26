package joshie.progression.gui;

public interface IRenderOverlay {

    boolean isVisible();

    boolean keyTyped(char character, int key);

    /** Return true if we clicked, and should not process further **/
    boolean mouseClicked(int mouseX, int mouseY, int button);

    void draw(int x, int y);

}
