package joshie.progression.api.criteria;

import java.util.UUID;

public interface IFieldProvider {
    /** Return an unlocalised name **/
    public String getUnlocalisedName();
    
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
            if (o == null || !(o instanceof IProgressionReward)) return false;

            IProgressionReward that = (IProgressionReward) o;
            return getUniqueID() != null ? getUniqueID().equals(that.getUniqueID()) : that.getUniqueID() == null;

        }

         @Override
         public int hashCode() {
            return getUniqueID() != null ? getUniqueID().hashCode() : 0;
         }
     * **/
    public UUID getUniqueID();
    
    /** Return a colour to display this thingy as **/
    public int getColor();

    /** Return a description of this thingy,
     *  this is to be displayed in display mode */
    public String getDescription();

    /** Called before anything is drawn
     *  Use this if you need to perform checks,
     *  Or validate things before stuff is drawn*/
    public void updateDraw();
}
