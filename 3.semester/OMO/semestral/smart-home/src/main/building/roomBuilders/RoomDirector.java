package main.building.roomBuilders;

import main.building.Room;

public final class RoomDirector {

    public Room createBathroom(RoomBuilder builder) {
        return builder.setWindows()
                .setBlinds()
                .setSmartLightning()
                .getResult();
    }

    public Room createBathroomForWinterHouse(RoomBuilder builder) {
        return builder.setWindows()
                .setBlinds()
                .setBoiler()
                .setWashingMachine()
                .setSmartLightning()
                .getResult();
    }

    public Room createChildrenBedroom(RoomBuilder builder) {
        return builder.setWindows()
                .setWindows()
                .setBlinds()
                .setBlinds()
                .setAC()
                .setGamingConsole()
                .setSmartLightning()
                .setTV()
                .getResult();
    }

    public Room createParentsBedroom(RoomBuilder builder) {
        return builder.setWindows()
                .setWindows()
                .setBlinds()
                .setBlinds()
                .setTV()
                .setAC()
                .setSmartLightning()
                .getResult();
    }

    public Room createLivingRoom(RoomBuilder builder) {
        return builder.setWindows()
                .setBlinds()
                .setAC()
                .setTV()
                .setSmartLightning()
                .getResult();
    }

    public Room createKitchen(RoomBuilder builder) {
        return builder.setWindows()
                .setBlinds()
                .setAC()
                .setMicrowave()
                .setRefrigerator()
                .setSmartLightning()
                .setStove()
                .setWashingMachine()
                .getResult();
    }

    public Room createKitchenForWinterHouse(RoomBuilder builder) {
        return builder.setWindows()
                .setBlinds()
                .setAC()
                .setMicrowave()
                .setRefrigerator()
                .setSmartLightning()
                .setStove()
                .getResult();
    }

    public Room createGarage(RoomBuilder builder) {
        return builder.setWindows()
                .setBlinds()
                .setSmartLightning()
                .setBoiler()
                .getResult();
    }

    public Room createGarageForWinter(RoomBuilder builder) {
        return builder.setWindows()
                .setBlinds()
                .setSmartLightning()
                .getResult();
    }
}
