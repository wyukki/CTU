package main.appliances.API;

import main.appliances.TV;

public final class TvAPI {
    private final TV tv;

    public TvAPI(TV tv) {
        this.tv = tv;
    }

    public void turnOnTV() {
        this.tv.turnOn();
    }

    public void turnOffTV() {
        this.tv.turnOff();
    }

}
