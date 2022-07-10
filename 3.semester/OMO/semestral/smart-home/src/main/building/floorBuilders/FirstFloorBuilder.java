package main.building.floorBuilders;

import main.building.Floor;
import main.building.roomBuilders.Builder;
import main.building.roomBuilders.RoomDirector;

public final class FirstFloorBuilder implements FloorBuilder {

    private final Floor firstFloor = new Floor(1);
    private final RoomDirector roomDirector = new RoomDirector();

    @Override
    public FloorBuilder setBathroomForWinterHouse() {
        return this;
    }

    @Override
    public FloorBuilder setBathroomForSummerHouse() {
        firstFloor.addRoom(roomDirector.createBathroom(new Builder("Bathroom")));
        return this;
    }

    @Override
    public FloorBuilder setChildrenBedroom() {
        firstFloor.addRoom(roomDirector.createChildrenBedroom(new Builder("Children bedroom")));
        return this;
    }

    @Override
    public FloorBuilder setParentsBedroom() {
        firstFloor.addRoom(roomDirector.createParentsBedroom(new Builder("Parents bedroom")));
        return this;
    }

    @Override
    public FloorBuilder setKitchenForWinterHouse() {
        return this;
    }

    @Override
    public FloorBuilder setKitchenForSummerHouse() {
        firstFloor.addRoom(roomDirector.createKitchen(new Builder("Kitchen")));
        return this;
    }

    @Override
    public FloorBuilder setGarageForSummerHouse() {
        firstFloor.addRoom(roomDirector.createGarage(new Builder("Garage")));
        return this;
    }

    @Override
    public FloorBuilder setGarageForWinterHouse() {
        return this;
    }

    @Override
    public FloorBuilder setLivingRoom() {
        firstFloor.addRoom(roomDirector.createLivingRoom(new Builder("Living room")));
        return this;
    }

    @Override
    public Floor getResult() {
        return firstFloor;
    }
}
