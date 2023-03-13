package cz.cvut.fel.ear.meetingroomreservation.dto;

import cz.cvut.fel.ear.meetingroomreservation.model.Floor;

import java.util.List;

public class BuildingDTO {
    private String name;
    private String address;
    private List<FloorDTO> floors;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<FloorDTO> getFloors() {
        return floors;
    }

    public void setFloors(List<FloorDTO> floors) {
        this.floors = floors;
    }
}
