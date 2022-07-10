package main.appliances.API;

import main.appliances.Microwave;

public final class MicrowaveAPI {
    private final Microwave microwave;

    public MicrowaveAPI(Microwave microwave) {
        this.microwave = microwave;
    }

    public void cookInMicrowave() {
        this.microwave.turnOn();
    }

    public void stopCookingInMicrowave() {
        this.microwave.turnOff();
    }

}
