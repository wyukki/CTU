package main.building;

import java.util.ArrayList;
import java.util.List;

public final class House {
    private final List<Floor> floors = new ArrayList<>();

    public void addFloor(Floor floor) {
        this.floors.add(floor);
    }

    public List<Floor> getFloors() {
        return floors;
    }
}
