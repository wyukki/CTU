package main.building.roomBuilders;

import main.building.Room;

public interface RoomBuilder {
    RoomBuilder setWindows();

    RoomBuilder setBlinds();

    RoomBuilder setAC();

    RoomBuilder setBoiler();

    RoomBuilder setGamingConsole();

    RoomBuilder setMicrowave();

    RoomBuilder setRefrigerator();

    RoomBuilder setSmartLightning();

    RoomBuilder setStove();

    RoomBuilder setTV();

    RoomBuilder setWashingMachine();

    Room getResult();
}
