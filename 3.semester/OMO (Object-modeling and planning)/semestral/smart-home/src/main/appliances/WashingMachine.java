package main.appliances;

import main.appliances.API.WashingMachineAPI;
import main.appliances.creators.TypeOfAppliance;

public final class WashingMachine extends Appliance {

    private final WashingMachineAPI washingMachineAPI = new WashingMachineAPI(this);

    public WashingMachine(TypeOfAppliance type, String name, int functionality,
                          double MIN_CONSUMPTION, double ACTIVE_CONSUMPTION) {
        super(type, name, functionality, MIN_CONSUMPTION, ACTIVE_CONSUMPTION);
    }

    public WashingMachineAPI getWashingMachineAPI() {
        return washingMachineAPI;
    }
}
