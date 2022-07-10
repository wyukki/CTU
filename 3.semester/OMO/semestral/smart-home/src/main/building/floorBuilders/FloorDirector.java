package main.building.floorBuilders;

import main.building.Floor;

public final class FloorDirector {
    public Floor createGroundFloor(GroundFloorBuilder builder) {
        return builder.setBathroomForSummerHouse()
                .setKitchenForSummerHouse()
                .setLivingRoom()
                .setGarageForSummerHouse()
                .getResult();
    }

    public Floor createFirstFloor(FirstFloorBuilder builder) {
        return builder.setBathroomForSummerHouse()
                .setChildrenBedroom()
                .setParentsBedroom()
                .getResult();
    }

    public Floor createGroundFloorForWinter(GroundFloorBuilder builder) {
        return builder.setBathroomForWinterHouse()
                .setParentsBedroom()
                .setChildrenBedroom()
                .setKitchenForWinterHouse()
                .setLivingRoom()
                .setGarageForWinterHouse()
                .getResult();
    }
}
