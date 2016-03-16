package joshie.progression.api;

/** Implement this on triggers that are able to be canceled **/
public interface ICancelable {
    /** Returns whether or not this trigger is currently canceled **/
    public boolean isCanceling();
    
    /** Sets this trigger to be canceled or not **/
    public void setCanceling(boolean isCanceled);
}
