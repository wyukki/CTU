package main.appliances;

import main.appliances.API.GamingConsoleAPI;
import main.appliances.creators.TypeOfAppliance;

public final class GamingConsole extends Appliance {
    private final GamingConsoleAPI gamingConsoleAPI = new GamingConsoleAPI(this);

    public GamingConsole(TypeOfAppliance type, String appliance, int functionality,
                         double MIN_CONSUMPTION, double ACTIVE_CONSUMPTION) {
        super(type, appliance, functionality, MIN_CONSUMPTION, ACTIVE_CONSUMPTION);
    }

    public GamingConsoleAPI getGamingConsoleAPI() {
        return gamingConsoleAPI;
    }
}
