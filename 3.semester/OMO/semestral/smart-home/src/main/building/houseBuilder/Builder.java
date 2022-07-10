package main.building.houseBuilder;

import main.building.House;
import main.building.floorBuilders.FirstFloorBuilder;
import main.building.floorBuilders.FloorDirector;
import main.building.floorBuilders.GroundFloorBuilder;

public final class Builder implements HouseBuilder {

    private final House house = new House();
    private final FloorDirector director = new FloorDirector();
    @Override
    public HouseBuilder setGroundFloor() {
        house.addFloor(director.createGroundFloor(new GroundFloorBuilder()));
        return this;
    }

    @Override
    public HouseBuilder setFirstFloor() {
        house.addFloor(director.createFirstFloor(new FirstFloorBuilder()));
        return this;
    }

    @Override
    public HouseBuilder setGroundFloorForWinter() {
        house.addFloor(director.createGroundFloorForWinter(new GroundFloorBuilder()));
        return this;
    }

    @Override
    public House getResult() {
        return house;
    }
}
