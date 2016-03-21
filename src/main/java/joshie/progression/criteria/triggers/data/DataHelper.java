package joshie.progression.criteria.triggers.data;

import joshie.progression.api.ITriggerData;
import joshie.progression.api.data.IDataHelper;
import joshie.progression.handlers.APIHandler;

public class DataHelper implements IDataHelper {
    @Override
    public boolean getBooleanData(ITriggerData iTriggerData) {
        return ((DataBoolean) iTriggerData).completed;
    }

    @Override
    public void setBooleanData(ITriggerData iTriggerData, boolean value) {
        ((DataBoolean) iTriggerData).completed = value;
    }

    @Override
    public ITriggerData newData(String data) {
        return APIHandler.newData(data);
    }

    @Override
    public int getDualNumber1(ITriggerData existing) {
        return ((DataCrafting) existing).amountCrafted;
    }

    @Override
    public int getDualNumber2(ITriggerData existing) {
        return ((DataCrafting) existing).timesCrafted;
    }

    @Override
    public void setDualData(ITriggerData existing, int number1, int number2) {
        DataCrafting crafting = ((DataCrafting) existing);
        crafting.amountCrafted = number1;
        crafting.timesCrafted = number2;
    }
}
