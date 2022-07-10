package main.building.houseBuilder;

import main.building.House;

public final class HouseDirector {

    public House createSummerHouse(Builder builder) {
        return builder.setGroundFloor().
                setFirstFloor()
                .getResult();
    }

    public House createWinterHouse(Builder builder) {
        return builder.setGroundFloorForWinter()
                .getResult();
    }
}
