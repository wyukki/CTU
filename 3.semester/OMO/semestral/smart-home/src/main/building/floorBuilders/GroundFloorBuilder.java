package main.building.floorBuilders;

import main.building.Floor;
import main.building.roomBuilders.Builder;
import main.building.roomBuilders.RoomDirector;

public final class GroundFloorBuilder implements FloorBuilder {

    private final Floor groundFloor = new Floor(0);
    private final RoomDirector roomDirector = new RoomDirector();

    @Override
    public FloorBuilder setBathroomForWinterHouse() {
        groundFloor.addRoom(roomDirector.createBathroomForWinterHouse(new Builder("Bathroom")));
        return this;
    }

    @Override
    public FloorBuilder setBathroomForSummerHouse() {
        groundFloor.addRoom(roomDirector.createBathroom(new Builder("Bathroom")));
        return this;
    }

    @Override
    public FloorBuilder setKitchenForWinterHouse() {
        groundFloor.addRoom(roomDirector.createKitchenForWinterHouse(new Builder("Kitchen")));
        return this;
    }

    @Override
    public FloorBuilder setKitchenForSummerHouse() {
        groundFloor.addRoom(roomDirector.createKitchen(new Builder("Kitchen")));
        return this;
    }

    @Override
    public FloorBuilder setGarageForSummerHouse() {
        groundFloor.addRoom(roomDirector.createGarage(new Builder("Garage")));
        return this;
    }

    @Override
    public FloorBuilder setGarageForWinterHouse() {
        groundFloor.addRoom(roomDirector.createGarageForWinter(new Builder("Garage")));
        return this;
    }

    @Override
    public FloorBuilder setLivingRoom() {
        groundFloor.addRoom(roomDirector.createLivingRoom(new Builder("Living room")));
        return this;
    }

    @Override
    public FloorBuilder setChildrenBedroom() {
        groundFloor.addRoom(roomDirector.createChildrenBedroom(new Builder("Children bedroom")));
        return this;
    }

    @Override
    public FloorBuilder setParentsBedroom() {
        groundFloor.addRoom(roomDirector.createParentsBedroom(new Builder("Parents bedroom")));
        return this;
    }


    @Override
    public Floor getResult() {
        return groundFloor;
    }
}
