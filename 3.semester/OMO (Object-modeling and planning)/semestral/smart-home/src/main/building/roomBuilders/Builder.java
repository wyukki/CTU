package main.building.roomBuilders;

import main.appliances.Appliance;
import main.appliances.creators.AppliancesCreator;
import main.appliances.creators.ElectricityApplianceCreator;
import main.appliances.creators.GasApplianceCreator;
import main.building.Room;
import main.roomElements.Blinds;
import main.roomElements.Window;

public final class Builder implements RoomBuilder {

    private final Room room;
    private final AppliancesCreator gasApplianceCreator = new GasApplianceCreator();
    private final AppliancesCreator electricityApplianceCreator = new ElectricityApplianceCreator();

    public Builder(String roomName) {
        this.room = new Room(roomName);
    }

    @Override
    public RoomBuilder setWindows() {
        room.addWindow(new Window());
        return this;
    }

    @Override
    public RoomBuilder setBlinds() {
        room.addBlinds(new Blinds());
        return this;
    }

    @Override
    public RoomBuilder setAC() {
        Appliance AC = electricityApplianceCreator.createAppliance("AC");
        room.addAppliance(AC);
        return this;
    }

    @Override
    public RoomBuilder setBoiler() {
        Appliance boiler = gasApplianceCreator.createAppliance("Boiler");
        room.addAppliance(boiler);
        return this;
    }

    @Override
    public RoomBuilder setGamingConsole() {
        Appliance gamingConsole = electricityApplianceCreator.createAppliance("Gaming console");
        room.addAppliance(gamingConsole);
        return this;
    }

    @Override
    public RoomBuilder setMicrowave() {
        Appliance microwave = electricityApplianceCreator.createAppliance("Microwave");
        room.addAppliance(microwave);
        return this;
    }

    @Override
    public RoomBuilder setRefrigerator() {
        Appliance refrigerator = electricityApplianceCreator.createAppliance("Refrigerator");
        room.addAppliance(refrigerator);
        return this;
    }

    @Override
    public RoomBuilder setSmartLightning() {
        Appliance smartLightning = electricityApplianceCreator.createAppliance("Smart lightning");
        room.addAppliance(smartLightning);
        return this;
    }

    @Override
    public RoomBuilder setStove() {
        Appliance stove = gasApplianceCreator.createAppliance("Stove");
        room.addAppliance(stove);
        return this;
    }

    @Override
    public RoomBuilder setTV() {
        Appliance TV = electricityApplianceCreator.createAppliance("TV");
        room.addAppliance(TV);
        return this;
    }

    @Override
    public RoomBuilder setWashingMachine() {
        Appliance washingMachine = electricityApplianceCreator.createAppliance("Washing machine");
        room.addAppliance(washingMachine);
        return this;
    }

    @Override
    public Room getResult() {
        return this.room;
    }
}
