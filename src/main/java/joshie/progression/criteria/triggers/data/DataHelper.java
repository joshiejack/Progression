package joshie.progression.criteria.triggers.data;

import joshie.progression.api.IDataHelper;
import joshie.progression.api.criteria.IProgressionTriggerData;
import joshie.progression.handlers.APIHandler;

public class DataHelper implements IDataHelper {
    @Override
    public boolean getBooleanData(IProgressionTriggerData iTriggerData) {
        return ((DataBoolean) iTriggerData).completed;
    }

    @Override
    public void setBooleanData(IProgressionTriggerData iTriggerData, boolean value) {
        ((DataBoolean) iTriggerData).completed = value;
    }

    @Override
    public IProgressionTriggerData newData(String data) {
        return APIHandler.newData(data);
    }

    @Override
    public int getDualNumber1(IProgressionTriggerData existing) {
        return ((DataCrafting) existing).amountCrafted;
    }

    @Override
    public int getDualNumber2(IProgressionTriggerData existing) {
        return ((DataCrafting) existing).timesCrafted;
    }

    @Override
    public void setDualData(IProgressionTriggerData existing, int number1, int number2) {
        DataCrafting crafting = ((DataCrafting) existing);
        crafting.amountCrafted = number1;
        crafting.timesCrafted = number2;
    }
}
