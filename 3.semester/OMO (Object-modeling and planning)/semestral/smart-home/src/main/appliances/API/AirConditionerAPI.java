package main.appliances.API;

import main.appliances.AirConditioner;

public final class AirConditionerAPI {

    private final AirConditioner ac;

    public AirConditionerAPI(AirConditioner ac) {
        this.ac = ac;
    }

    public void turnOnAC() {
        this.ac.turnOn();
    }

    public void turnOffAC() {
        this.ac.turnOff();
    }
}
