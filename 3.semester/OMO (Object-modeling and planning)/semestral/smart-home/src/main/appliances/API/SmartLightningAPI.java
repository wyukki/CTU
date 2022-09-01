package main.appliances.API;

import main.appliances.SmartLightning;

public final class SmartLightningAPI {
    private final SmartLightning smartLightning;

    public SmartLightningAPI(SmartLightning smartLightning) {
        this.smartLightning = smartLightning;
    }

    public void turnOnSmartLightning() {
        smartLightning.turnOn();
    }

    public void turnOffSmartLightning() {
        smartLightning.turnOff();
    }
}
