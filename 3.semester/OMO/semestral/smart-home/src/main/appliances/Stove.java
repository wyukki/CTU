package main.appliances;

import main.appliances.API.StoveAPI;
import main.appliances.creators.TypeOfAppliance;

public final class Stove extends Appliance {

    private final StoveAPI stoveAPI = new StoveAPI(this);

    public Stove(TypeOfAppliance type, String name, int functionality,
                 double MIN_CONSUMPTION, double ACTIVE_CONSUMPTION ) {
        super(type, name, functionality, MIN_CONSUMPTION, ACTIVE_CONSUMPTION);
    }

    public StoveAPI getStoveAPI() {
        return stoveAPI;
    }
}
