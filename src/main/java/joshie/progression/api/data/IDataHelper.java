package joshie.progression.api.data;

import joshie.progression.api.ITriggerData;

public interface IDataHelper {
    /** Returns a boolean value from the trigger data **/
    boolean getBooleanData(ITriggerData iTriggerData);

    /** Sets the trigger data boolean value **/
    public void setBooleanData(ITriggerData iTriggerData, boolean value);

    /** Use this to create the data type you desire
     *  Default options are "boolean", "count", and "crafting" (two numbers) **/
    public ITriggerData newData(String data);

    /** Returns the first number in the crafting type **/
    public int getDualNumber1(ITriggerData existing);
    
    /** Returns the second number in the crafting type **/
    public int getDualNumber2(ITriggerData existing);

    /** Sets the first and second number in the crafting type **/
    public void setDualData(ITriggerData existing, int number1, int number2);
}
