package main.appliances;

import main.appliances.API.MicrowaveAPI;
import main.appliances.creators.TypeOfAppliance;

public final class Microwave extends Appliance {
    private final MicrowaveAPI microwaveAPI = new MicrowaveAPI(this);

    public Microwave(TypeOfAppliance type, String appliance, int functionality,
                     double MIN_CONSUMPTION, double ACTIVE_CONSUMPTION) {
        super(type, appliance, functionality, MIN_CONSUMPTION, ACTIVE_CONSUMPTION);
    }

    public MicrowaveAPI getMicrowaveAPI() {
        return microwaveAPI;
    }
}
