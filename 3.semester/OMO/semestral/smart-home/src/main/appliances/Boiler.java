package main.appliances;

import main.appliances.API.BoilerAPI;
import main.appliances.creators.TypeOfAppliance;

public final class Boiler extends Appliance {
    private final BoilerAPI boilerAPI = new BoilerAPI(this);

    public Boiler(TypeOfAppliance type, String name, int functionality,
                  double MIN_CONSUMPTION, double ACTIVE_CONSUMPTION) {
        super(type, name, functionality, MIN_CONSUMPTION, ACTIVE_CONSUMPTION);
    }

    public BoilerAPI getBoilerAPI() {
        return boilerAPI;
    }
}
