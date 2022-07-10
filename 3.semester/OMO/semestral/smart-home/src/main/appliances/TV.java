package main.appliances;

import main.appliances.API.TvAPI;
import main.appliances.creators.TypeOfAppliance;

public final class TV extends Appliance {
    private final TvAPI tvAPI = new TvAPI(this);

    public TV(TypeOfAppliance type, String appliance, int functionality,
              double MIN_CONSUMPTION, double ACTIVE_CONSUMPTION) {
        super(type, appliance, functionality, MIN_CONSUMPTION, ACTIVE_CONSUMPTION);
    }

    public TvAPI getTvAPI() {
        return tvAPI;
    }
}
