package joshie.progression.api;

/** Implement this on rewards, conditions, filters or conditions, 
 *  That have special init status to load up other variables after being read.*/
public interface IInitAfterRead {
        /** Perform the init **/
    public void init();
}
