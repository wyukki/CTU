package main.building.houseBuilder;

import main.building.House;

public interface HouseBuilder {
    HouseBuilder setGroundFloor();
    HouseBuilder setFirstFloor();
    HouseBuilder setGroundFloorForWinter();
    House getResult();
}
