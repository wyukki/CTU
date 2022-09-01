package main.appliances.API;

import main.appliances.Boiler;

public final class BoilerAPI {
    private final Boiler boiler;

    public BoilerAPI(Boiler boiler) {
        this.boiler = boiler;
    }

    public void turnOnBoiler(){
        this.boiler.turnOn();
    }

    public void turnOffBoiler(){
        this.boiler.turnOff();
    }

}

