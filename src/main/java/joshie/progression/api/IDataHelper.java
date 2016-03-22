package joshie.progression.api;

import joshie.progression.api.criteria.IProgressionTriggerData;

public interface IDataHelper {
    /** Returns a boolean value from the trigger data **/
    boolean getBooleanData(IProgressionTriggerData iTriggerData);

    /** Sets the trigger data boolean value **/
    public void setBooleanData(IProgressionTriggerData iTriggerData, boolean value);

    /** Use this to create the data type you desire
     *  Default options are "boolean", "count", and "crafting" (two numbers) **/
    public IProgressionTriggerData newData(String data);

    /** Returns the first number in the crafting type **/
    public int getDualNumber1(IProgressionTriggerData existing);
    
    /** Returns the second number in the crafting type **/
    public int getDualNumber2(IProgressionTriggerData existing);

    /** Sets the first and second number in the crafting type **/
    public void setDualData(IProgressionTriggerData existing, int number1, int number2);
}
