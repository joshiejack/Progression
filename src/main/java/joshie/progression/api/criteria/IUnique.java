package joshie.progression.api.criteria;

import java.util.UUID;

public interface IUnique {
    /** Return the localised name for this thing **/
    public String getLocalisedName();

    /** Return the uniqueid for this object, ensure that you add equals
     * and hashcode that match this example if you're creating your own,
     * switch out reward for whatever type you're using
     *
     *
         @Override
         public boolean equals(Object o) {
             if (this == o) return true;
             if (o == null || !(o instanceof IRewardProvider)) return false;

         IRewardProvider that = (IRewardProvider) o;
             return getUniqueID() != null ? getUniqueID().equals(that.getUniqueID()) : that.getUniqueID() == null;
         }

         @Override
         public int hashCode() {
            return getUniqueID() != null ? getUniqueID().hashCode() : 0;
         }
      * **/
    public UUID getUniqueID();

    /** Return true if this items should be displayed to the players,
     *  should be configurable **/
    public boolean isVisible();
}
