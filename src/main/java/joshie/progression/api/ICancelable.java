package joshie.progression.api;

public interface ICancelable {
    public boolean isCanceling();
    public boolean isCancelable();
    public void setCanceling(boolean isCanceled);
}
