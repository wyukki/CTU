package main.appliances.API;

import main.appliances.WashingMachine;

public final class WashingMachineAPI {

    private final WashingMachine washingMachine;

    public WashingMachineAPI(WashingMachine washingMachine) {
        this.washingMachine = washingMachine;
    }

    public void doLaundry() {
        this.washingMachine.turnOn();
    }

    public void finishLaundry() {
        this.washingMachine.turnOff();
    }

}
