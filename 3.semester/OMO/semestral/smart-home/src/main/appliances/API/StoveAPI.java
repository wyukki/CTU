package main.appliances.API;

import main.appliances.Stove;

public final class StoveAPI {
    private final Stove stove;

    public StoveAPI(Stove stove) {
        this.stove = stove;
    }

    public void cookOnStove() {
        stove.turnOn();
    }

    public void stopCookingOnStove() {
        stove.turnOff();
    }

}
