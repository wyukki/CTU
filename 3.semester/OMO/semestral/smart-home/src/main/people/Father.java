package main.people;

import main.appliances.Appliance;
import main.building.Room;

import java.util.ArrayList;
import java.util.List;

public final class Father extends Person {
    private final List<Appliance> brokenAppliances = new ArrayList<>();

    public Father(Family identification, Room room) {
        super(identification, room);
    }

    public void addBrokenAppliance(Appliance appliance) {
        this.brokenAppliances.add(appliance);
    }

    public List<Appliance> getBrokenAppliances() {
        return brokenAppliances;
    }
}
