package main.appliances;

import main.appliances.API.AirConditionerAPI;
import main.appliances.creators.TypeOfAppliance;

public final class AirConditioner extends Appliance {
    private final AirConditionerAPI acAPI = new AirConditionerAPI(this);

    public AirConditioner(TypeOfAppliance type, String appliance, int functionality,
                          double MIN_CONSUMPTION, double ACTIVE_CONSUMPTION) {
        super(type, appliance, functionality, MIN_CONSUMPTION, ACTIVE_CONSUMPTION);
    }

    public AirConditionerAPI getAcAPI() {
        return acAPI;
    }
}
