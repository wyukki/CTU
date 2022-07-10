package main.appliances;

import main.appliances.API.SmartLightningAPI;
import main.appliances.creators.TypeOfAppliance;

public final class SmartLightning extends Appliance {
    private final SmartLightningAPI smartLightningAPI = new SmartLightningAPI(this);

    public SmartLightning(TypeOfAppliance type, String appliance, int functionality,
                          double MIN_CONSUMPTION, double ACTIVE_CONSUMPTION) {
        super(type, appliance, functionality, MIN_CONSUMPTION, ACTIVE_CONSUMPTION);
    }

    public SmartLightningAPI getSmartLightningAPI() {
        return smartLightningAPI;
    }

}
