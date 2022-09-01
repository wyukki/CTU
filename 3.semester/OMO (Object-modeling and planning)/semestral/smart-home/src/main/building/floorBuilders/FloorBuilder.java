package main.building.floorBuilders;

import main.building.Floor;

public interface FloorBuilder {
    FloorBuilder setBathroomForWinterHouse();
    FloorBuilder setBathroomForSummerHouse();
    FloorBuilder setChildrenBedroom();
    FloorBuilder setParentsBedroom();
    FloorBuilder setKitchenForWinterHouse();
    FloorBuilder setKitchenForSummerHouse();
    FloorBuilder setGarageForSummerHouse();
    FloorBuilder setGarageForWinterHouse();
    FloorBuilder setLivingRoom();
    Floor getResult();
}
