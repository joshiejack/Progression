package joshie.progression.gui.editors;

public abstract class GuiBaseEditorRule<T> extends GuiBaseEditor {
    public abstract T get();
    public abstract void set(T t);

    @Override
    public Object getKey() {
        return get();
    }
}
